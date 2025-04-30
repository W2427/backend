from flask import Flask, Blueprint, jsonify, request
import os
import argparse
from dotenv import load_dotenv
#from report import *
import redis
from ..common.db import init_db, close_db

# 创建蓝图对象
#api_bp = Blueprint('api', __name__)

#@api_bp.route('/report', methods=['POST'])
#def handle_report():
#    try:
#        response = generate_report_handler(request.json)
#        return jsonify(response)
#    except Exception as e:
#        return jsonify({'error': str(e)}), 500

def create_app(env='testing'):
    app = Flask(__name__)

    # 先加载配置再初始化组件
    parser = argparse.ArgumentParser()
    parser.add_argument('--env', type=str, default=env)
    args, _ = parser.parse_known_args()
    print(args)

    current_script_path = os.path.abspath(__file__)
    project_dir = os.path.dirname(current_script_path)
    env_file = f'{project_dir}/../config/.env.{args.env}'
    print(env_file)
    if os.path.exists(env_file):
        load_dotenv(env_file, override=True)

    # 从环境变量加载所有大写的配置项
    app.config.update({k: v for k, v in os.environ.items() if k.isupper()})

    init_db()

    app.teardown_appcontext(close_db)

    # 初始化Redis连接
    if 'REDIS_URL' in app.config:
        app.extensions['redis'] = redis.Redis.from_url(app.config['REDIS_URL'])

    # 注册蓝图
    from .report.generate_report import report_bp
    app.register_blueprint(report_bp, url_prefix='')

    return app

if __name__ == '__main__':
    app = create_app()
    app.run(port=app.config.get('PORT', 9000))
