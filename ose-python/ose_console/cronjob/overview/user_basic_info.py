from ose_console.common.db import get_db_session
from sqlalchemy import text
from datetime import date, datetime


def stats_user_total():
    session = get_db_session()
    today = date.today()
    month = today.year*100 + today.month
    print(month)
    # 获取全部用户
    row = session.execute(text("select count(*) from saint_whale_auth.users where type = 'user' and status != 'DELETED'")).fetchone()
    update_user_sql = "insert into saint_whale_auth.overview_total_by_date(`type`,`month`,`total`) values (1, :month, :total) on duplicate key update total=:total"
    session.execute(text(update_user_sql), {"total":row[0], "month": month})
    session.commit()


def stats_user_basic_info():
    print('###########################')
    session = get_db_session()

    # 获取全部用户
    rows = session.execute(text("select id, birthday, onboarding_date from saint_whale_auth.users where type = 'user' and status != 'DELETED'")).fetchall()
    for row in rows:
        user_id = row[0]
        age_group = calculate_age_group(row[1])
        onboarding_month, service_year = calculate_onboarding_date(row[2])
        update_user_sql = "update saint_whale_auth.users set age_group=:age_group, service_years=:service_year, onboarding_month=:onboarding_month where id=:user_id"
        session.execute(text(update_user_sql), {"age_group":age_group, "service_year":service_year, "onboarding_month":onboarding_month, "user_id":user_id})
        session.commit()

def calculate_age_group(birthday):
    if birthday is None:
        return ''
    today = date.today()
    #birthday = datetime.strptime(birthday_str, '%Y-%m-%d %H:%M:%S').date()
    age = today.year - birthday.year - ((today.month, today.day) < (birthday.month, birthday.day))
    age_group = ''
    if age < 25:
        age_group = 'Under 25'
    elif age >= 25 and age <= 30:
        age_group = '25-30'
    elif age > 30 and age <= 45:
        age_group = '31-45'
    else:
        age_group = 'Above 45'
    return age_group

def calculate_onboarding_date(onboarding_date):
    if onboarding_date is None:
        return 0, ''
    today = date.today()
    #onboarding_date = datetime.strptime(onboarding_date_str, '%Y-%m-%d %H:%M:%S').date()
    onboarding_month = onboarding_date.year*100 + onboarding_date.month
    company_age = today.year - onboarding_date.year - ((today.month, today.day) < (onboarding_date.month, onboarding_date.day))

    print(onboarding_month, company_age)

    service_year = ''
    if company_age < 1:
        service_year = '<1yr'
    elif company_age >= 1 and company_age < 2:
        service_year = '1yr'
    elif company_age >= 2 and company_age < 3:
        servive_year = '2yrs'
    elif company_age >= 3 and company_age < 4:
        servive_year = '3yrs'
    elif company_age >= 4 and company_age < 5:
        servive_year = '4yrs'
    elif company_age >= 5 and company_age < 6:
        servive_year = '5yrs'
    elif company_age >= 6 :
        servive_year = '>=6yrs'
    return onboarding_month, service_year

