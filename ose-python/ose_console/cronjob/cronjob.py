import os
import argparse
from dotenv import load_dotenv
from apscheduler.schedulers.blocking import BlockingScheduler
from ose_console.cronjob.overview.user_basic_info import stats_user_basic_info,stats_user_total
from ..common.db import init_db, close_db


if __name__ == '__main__':
    # 先加载配置再初始化组件
    parser = argparse.ArgumentParser()
    parser.add_argument('--env', type=str, default='testing')
    args, _ = parser.parse_known_args()
    print(args)

    current_script_path = os.path.abspath(__file__)
    project_dir = os.path.dirname(current_script_path)
    env_file = f'{project_dir}/../config/.env.{args.env}'
    if os.path.exists(env_file):
        load_dotenv(env_file, override=True)

    init_db()

    # 创建调度器
    scheduler = BlockingScheduler()

    # 每天0点执行（使用 cron 表达式）
    #scheduler.add_job(job_daily, 'cron', hour=0, minute=0)

    # 每隔3分钟执行（使用 interval 触发器）
    scheduler.add_job(stats_user_basic_info, 'interval', minutes=1)

    # 每隔3分钟执行（使用 interval 触发器）
    scheduler.add_job(stats_user_total, 'interval', minutes=1)

    try:
        scheduler.start()
    except KeyboardInterrupt:
        print("任务已停止")

