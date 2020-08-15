package com.xxscloud.videox.service.api


import com.google.inject.Inject
import com.google.inject.Singleton
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.dao.CommentDAO
import com.xxscloud.videox.dao.UserDAO
import com.xxscloud.videox.data.UserDO
import com.xxscloud.videox.data.UserDTO
import com.xxscloud.videox.exception.ExceptionMessageEnum
import com.xxscloud.videox.exception.ServiceException
import org.apache.commons.beanutils.BeanUtils
import java.util.*


@Singleton
class UserService @Inject constructor(private val userDAO: UserDAO, private val commentDAO: CommentDAO) {

    suspend fun login(account: String, password: String): UserDO {
        //获取用户信息
        var userInfo = userDAO.getByAccount(account)
        //注册
        if (userInfo == null) {
            userDAO.registered(account, password)
            userInfo = userDAO.getByAccount(account)
        }
        //用户信息
        userInfo?.let {
            if (it.password == password) {
                it.token = UUID.randomUUID().toString().replace("-", "")
                userDAO.updateToken(it.id, it.token)
                return it
            }
        }
        throw ServiceException(ExceptionMessageEnum.ACCOUNT_OR_PASSWORD_ERROR)
    }

    suspend fun getUserInfo(id: String): UserDTO? {
        val user = userDAO.getById(id)
        user?.let {
            val userDTO = UserDTO()
            BeanUtils.copyProperties(userDTO, user)
            userDTO.commentList = commentDAO.getCommentByUserId(it.id)
            return userDTO
        }
        return null
    }
}