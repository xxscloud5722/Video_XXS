package com.xxscloud.videox.core

class SnowFlake {

    companion object {
        private val W = IdWorker(1, 1, 1)
        fun getId(): String {
            return W.nextId().toString()
        }
    }


    class IdWorker(workerId: Long, datacenterId: Long, sequence: Long) {
        //下面两个每个5位，加起来就是10位的工作机器id
        val workerId //工作id
                : Long
        val datacenterId //数据id
                : Long

        //12位的序列号
        private var sequence: Long

        //初始时间戳
        private val twepoch = 1288834974657L

        //长度为5位
        private val workerIdBits = 5L
        private val datacenterIdBits = 5L

        //最大值
        private val maxWorkerId = -1L xor (-1L shl workerIdBits.toInt())
        private val maxDatacenterId = -1L xor (-1L shl datacenterIdBits.toInt())

        //序列号id长度
        private val sequenceBits = 12L

        //序列号最大值
        private val sequenceMask = -1L xor (-1L shl sequenceBits.toInt())

        //工作id需要左移的位数，12位
        private val workerIdShift = sequenceBits

        //数据id需要左移位数 12+5=17位
        private val datacenterIdShift = sequenceBits + workerIdBits

        //时间戳需要左移位数 12+5+5=22位
        private val timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits

        //上次时间戳，初始值为负数
        private var lastTimestamp = -1L

        val timestamp: Long
            get() = System.currentTimeMillis()

        /**
         * 下一个ID生成算法
         *
         * @return 下一个ID.
         */
        @Synchronized
        fun nextId(): Long {
            var timestamp = timeGen()

            //获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
            if (timestamp < lastTimestamp) {
                System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp)
                throw RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp))
            }

            //获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
            if (lastTimestamp == timestamp) {
                sequence = sequence + 1 and sequenceMask
                if (sequence == 0L) {
                    timestamp = tilNextMillis(lastTimestamp)
                }
            } else {
                sequence = 0
            }

            //将上次时间戳值刷新
            lastTimestamp = timestamp
            /**
             * 返回结果：
             * (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
             * (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
             * (workerId << workerIdShift) 表示将工作id左移相应位数
             * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
             * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
             */
            return timestamp - twepoch shl timestampLeftShift.toInt() or
                    (datacenterId shl datacenterIdShift.toInt()) or
                    (workerId shl workerIdShift.toInt()) or
                    sequence
        }

        /**
         * 获取时间戳，并与上次时间戳比较
         *
         * @param lastTimestamp 时间.
         * @return 时间.
         */
        private fun tilNextMillis(lastTimestamp: Long): Long {
            var timestamp = timeGen()
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen()
            }
            return timestamp
        }

        /**
         * 获取系统时间戳.
         *
         * @return 时间.
         */
        private fun timeGen(): Long {
            return System.currentTimeMillis()
        }

        init {
            // sanity check for workerId
            require(!(workerId > maxWorkerId || workerId < 0)) { String.format("worker Id can't be greater than %d or less than 0", maxWorkerId) }
            require(!(datacenterId > maxDatacenterId || datacenterId < 0)) { String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId) }
            System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                    timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId)
            this.workerId = workerId
            this.datacenterId = datacenterId
            this.sequence = sequence
        }
    }

}