from flask import Blueprint,jsonify,request
from ..prql_processor import process_prql
from ose_console.common.db import get_db_session
import os
from sqlalchemy import text
import sqlparse
from sqlparse.sql import Statement, Token, Identifier, IdentifierList
from sqlparse.tokens import Keyword, DML


report_bp = Blueprint('report', __name__)

allowed_tables = {'overview_total_by_date', 'drawing_record', 'project', 'drawing_plan_hour', 'users'}
allowed_max_rows = 500

def validate_sql_readonly(sql: str) -> bool:
    """校验 SQL 是否为只读查询（仅允许 SELECT）"""
    if not sql.token_first().value.upper() == 'SELECT':
        return False
    return True


# 只允许部分表访问
def validate_query_tables(sql: str) -> bool:
    tables = set()
    from_seen = False

    for token in sql.tokens:
        # 检测 FROM/JOIN 关键字
        if token.ttype is Keyword:
            token_val = token.value.upper()
            from_seen = token_val in ('FROM', 'JOIN', 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN')

        # 提取 FROM/JOIN 后的表名
        if from_seen and isinstance(token, (IdentifierList, Identifier)):
            # 递归提取所有 Identifier
            def extract_identifiers(t):
                if isinstance(t, IdentifierList):
                    for item in t.get_identifiers():
                        yield from extract_identifiers(item)
                elif isinstance(t, Identifier):
                    yield t
            for identifier in extract_identifiers(token):
                # 分割可能存在的 schema 前缀（例如 "public.user" -> user）
                print(identifier)
                name_parts = identifier.get_real_name().split('.')
                table_name = name_parts[-1].strip('`"').lower()  # 统一小写
                tables.add(table_name)

    print(tables)
    # 验证表名范围
    invalid_tables = [name for name in tables if name not in allowed_tables]
    if invalid_tables:
        return False
    return True


# 不允许查询超过指定行数
def validate_query_rows(results) -> bool:
    if len(results) > allowed_max_rows:
        return False
    return True


@report_bp.route('/overview', methods=['POST'])
def generate_report_handler():
    data = request.get_json(force=True, silent=True)
    if not data:
        return jsonify({'error': 'Missing PRQL query'}), 400
    prql_str = data.get('prqlStr')

    session = get_db_session()
    try:
        sql_query = process_prql(prql_str)
        parsed = sqlparse.parse(sql_query)
        stmt = parsed[0]
        if not validate_sql_readonly(stmt):
            return jsonify({'error': 'only select'}), 400
        if not validate_query_tables(stmt):
            return jsonify({'error': 'exist invalid tables'}), 400
        #session = SessionLocal()
        rows = session.execute(text(sql_query))
        session.commit()
        columns = rows.keys()
        if not validate_query_rows(columns):
            return jsonify({'error': 'exceed max rows'}), 400
        dict_list = [dict(zip(columns, row)) for row in rows]
        return jsonify({
            'sql': sql_query,
            'data': dict_list
        })

    except Exception as e:
        print(e)
        session.rollback()
        return jsonify({'error': str(e)}), 500
    finally:
        session.close()

