package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.Captcha;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图形验证码 CRUD 操作接口。
 */
@Transactional
public interface CaptchaRepository extends CrudRepository<Captcha, Long> {
}
