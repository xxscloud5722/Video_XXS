package com.xxscloud.videox.exception

class ThirdpartyException : RuntimeException {
    constructor(message: ExceptionMessageEnum) : super(message.code + "\n" + message.message)

    constructor(message: String) : super(message)
}