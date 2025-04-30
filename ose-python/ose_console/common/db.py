from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
import os


def init_db():
    global engine, SessionLocal

    print(os.environ['MYSQL_USER'])
    engine = create_engine(
        f"mysql+pymysql://{os.environ['MYSQL_USER']}:{os.environ['MYSQL_PASSWORD']}@{os.environ['MYSQL_HOST']}:{os.environ['MYSQL_PORT']}/{os.environ['MYSQL_DB']}",
        pool_size=5,
        max_overflow=10,
        pool_pre_ping=True,
        pool_recycle=3600
    )

    SessionLocal = scoped_session(
        sessionmaker(
            autocommit=False,
            autoflush=False,
            bind=engine,
            expire_on_commit=False  # 防止数据过期导致属性访问错误
        )
    )

    # 注册到 Flask 扩展系统
    #app.extensions['sqlalchemy'] = {
    #    'engine': engine,
    #    'session': SessionLocal
    #}

def get_db_session():
    #SessionLocal = current_app.extensions['sqlalchemy']['session']
    session = SessionLocal()
    return session
    #try:
    #    yield session
    #except Exception as e:
    #    session.rollback()
    #    raise
    #finally:
    #    session.close()  # 将会话归还连接池


def close_db(e=None):
    if engine:
        engine.dispose()
