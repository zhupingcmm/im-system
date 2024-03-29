CREATE TABLE IF NOT EXISTS im_user_data (
    id serial PRIMARY KEY,
    user_id varchar(20) NOT NULL,
    nick_name varchar(45) DEFAULT NULL,
    location varchar(20) NOT NULL,
    birth_day TIMESTAMP NOT NULL,
    password varchar(45) DEFAULT NULL,
    photo varchar(45) DEFAULT NULL,
    user_sex INTEGER check(user_sex in (0, 1)) NOT NULL,
    self_signature varchar(45) DEFAULT NULL,
    friend_allow_type INTEGER check(friend_allow_type in (0, 1)) DEFAULT 1 NOT NULL,
    disable_add_friend INTEGER check(disable_add_friend in (0, 1)) DEFAULT 0 NOT NULL,
    forbidden_flag INTEGER check(forbidden_flag in (0, 1)) DEFAULT 0 NOT NULL,
    silent_flag INTEGER check(silent_flag in (0, 1)) DEFAULT 0 NOT NULL,
    user_type INTEGER check(user_type in (1, 2, 3)) DEFAULT 1 NOT NULL,
    app_id varchar(45) DEFAULT NULL,
    is_active INTEGER check(is_active in (0, 1)) DEFAULT 1 NOT NULL,
    extra varchar(45) DEFAULT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );