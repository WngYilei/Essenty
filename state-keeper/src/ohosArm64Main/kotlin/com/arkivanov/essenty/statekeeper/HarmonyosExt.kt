package com.arkivanov.essenty.statekeeper

/**
 * 为 HarmonyOS 平台创建 StateKeeper 实例。
 *
 * 此实现提供内存中的状态保存和恢复。对于需要持久化的场景，
 * 可以使用 [savedState] 参数从持久化存储恢复状态，
 * 并使用 [onSave] 回调将状态保存到持久化存储。
 *
 * 注意：这是与 Android StateKeeper(SavedStateRegistry) 对等的 HarmonyOS 实现。
 * HarmonyOS 没有与 Android SavedStateRegistry 完全等价的 API，
 * 因此此实现使用回调模式，允许应用集成 HarmonyOS 的 Preferences 或其他持久化机制。
 *
 * @param savedState 从持久化存储恢复的状态数据（可选）
 * @param isSavingAllowed 在保存状态前调用的回调。
 * 当为 `true` 时将保存状态，否则不保存。默认值为 `true`。
 * @param onSave 当状态需要保存时调用的回调，接收序列化后的状态数据。
 * 应用应该将此数据保存到 HarmonyOS 的 Preferences 或其他持久化存储中。
 */
fun StateKeeper(
    savedState: SerializableContainer? = null,
    isSavingAllowed: () -> Boolean = { true },
    onSave: (SerializableContainer) -> Unit = {},
): StateKeeper {
    val dispatcher = StateKeeperDispatcher(savedState = savedState)

    // 创建一个包装器来拦截 save 调用
    return object : StateKeeper {
        override fun <T : Any> consume(key: String, strategy: kotlinx.serialization.DeserializationStrategy<T>): T? {
            return dispatcher.consume(key, strategy)
        }

        override fun <T : Any> register(
            key: String,
            strategy: kotlinx.serialization.SerializationStrategy<T>,
            supplier: () -> T?,
        ) {
            dispatcher.register(key, strategy, supplier)
        }

        override fun unregister(key: String) {
            dispatcher.unregister(key)
        }

        override fun isRegistered(key: String): Boolean {
            return dispatcher.isRegistered(key)
        }

        // 当需要保存状态时（例如在 onPause 生命周期）
        fun save() {
            if (isSavingAllowed()) {
                val state = dispatcher.save()
                onSave(state)
            }
        }
    }
}

/**
 * 为 HarmonyOS 创建一个简单的 StateKeeper 实例，不需要持久化。
 *
 * 此工厂函数创建一个仅在内存中保存状态的 StateKeeper，
 * 适用于不需要持久化状态的简单场景。
 *
 * @param discardSavedState 是否丢弃之前保存的状态，默认为 false
 */
fun StateKeeper(
    discardSavedState: Boolean = false,
): StateKeeper = StateKeeperDispatcher(
    savedState = if (discardSavedState) null else null
)
