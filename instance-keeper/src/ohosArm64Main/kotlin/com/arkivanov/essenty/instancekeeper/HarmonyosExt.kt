package com.arkivanov.essenty.instancekeeper

/**
 * 为鸿蒙平台创建 [InstanceKeeper] 实例。
 *
 * 鸿蒙平台与 Android 不同,没有内置的 ViewModelStore 机制。
 * 因此我们需要提供一个自定义的存储机制来保持实例。
 *
 * 这个实现使用一个简单的持有者模式,类似于 Android 的 ViewModel 实现,
 * 但需要调用者自行管理存储容器的生命周期。
 *
 * @param storage 用于存储 InstanceKeeper 的容器,由调用者管理生命周期
 * @param key 在存储中标识 InstanceKeeper 的键,默认值为 "InstanceKeeper"
 * @param discardRetainedInstances 是否丢弃并销毁之前保留的实例的标志,默认值为 `false`
 */
fun InstanceKeeper(
    storage: MutableMap<String, Any>,
    key: String = DEFAULT_KEY,
    discardRetainedInstances: Boolean = false,
): InstanceKeeper {
    @Suppress("UNCHECKED_CAST")
    var holder = storage[key] as? InstanceKeeperHolder

    if (holder == null || discardRetainedInstances) {
        holder = InstanceKeeperHolder()
        storage[key] = holder
    }

    return holder.dispatcher
}

/**
 * 默认的存储键
 */
private const val DEFAULT_KEY = "com.arkivanov.essenty.instancekeeper.InstanceKeeper"

/**
 * 内部持有类,用于包装 InstanceKeeperDispatcher
 * 这个类的作用类似于 Android 的 InstanceKeeperViewModel
 */
internal class InstanceKeeperHolder {
    var dispatcher: InstanceKeeperDispatcher = InstanceKeeperDispatcher()
        private set

    /**
     * 重新创建 dispatcher,销毁之前的实例
     */
    fun recreate() {
        dispatcher.destroy()
        dispatcher = InstanceKeeperDispatcher()
    }
}
