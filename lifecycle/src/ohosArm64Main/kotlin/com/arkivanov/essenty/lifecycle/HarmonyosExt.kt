package com.arkivanov.essenty.lifecycle

import com.arkivanov.essenty.lifecycle.Lifecycle as EssentyLifecycle

/**
 * HarmonyOS Lifecycle Support for Essenty
 *
 * This file provides placeholder functions for HarmonyOS (ohosArm64) platform support.
 * To enable full lifecycle integration with HarmonyOS Ability components, you need to
 * implement the actual bridge using HarmonyOS SDK APIs.
 *
 * ## Implementation Guide
 *
 * Due to the complexity of HarmonyOS SDK integration and potential licensing restrictions,
 * this module provides only the interface definitions. To implement the actual lifecycle
 * bridge, follow these steps:
 *
 * 1. **Add HarmonyOS SDK Dependency**:
 *    Ensure your project has access to HarmonyOS SDK libraries (ohos.jar).
 *
 * 2. **Create a Custom Bridge Implementation**:
 *    ```kotlin
 *    // In your HarmonyOS-specific source set
 *    import ohos.ability.Ability
 *    import ohos.ability.AbilityLifecycleExecutor
 *
 *    fun Ability.asEssentyLifecycle(): EssentyLifecycle {
 *        // Implementation that bridges HarmonyOS lifecycle to Essenty
 *        return HarmonyosLifecycleBridge(this)
 *    }
 *    ```
 *
 * 3. **Lifecycle State Mapping**:
 *    - INITIAL -> INITIALIZED
 *    - INACTIVE -> STARTED
 *    - ACTIVE -> RESUMED
 *    - STOPPED -> DESTROYED
 *
 * 4. **Register Lifecycle Listeners**:
 *    Use `AbilityLifecycleExecutor.registerLifecycleListener()` to observe
 *    lifecycle changes and forward them to Essenty Lifecycle callbacks.
 *
 * ## Alternative Approach
 *
 * If you prefer not to depend on HarmonyOS SDK in the lifecycle module, you can
 * create a separate adapter module in your application that depends on both Essenty
 * and HarmonyOS SDK:
 *
 * ```kotlin
 * // In your app's module
 * class HarmonyosLifecycleAdapter(
 *     private val ability: Ability
 * ) : LifecycleRegistry {
 *
 *     private val registry = LifecycleRegistry()
 *
 *     init {
 *         ability.lifecycleExecutor.registerLifecycleListener(object :
 *             AbilityLifecycleExecutor.LifecycleListener {
 *             override fun onAbilityStart(ability: Ability) {
 *                 registry.onCreate()
 *                 registry.onStart()
 *             }
 *             override fun onAbilityActive(ability: Ability) {
 *                 registry.onResume()
 *             }
 *             // ... handle other lifecycle events
 *         })
 *     }
 *
 *     // Delegate LifecycleRegistry methods to registry
 *     override val state: Lifecycle.State get() = registry.state
 *     override fun subscribe(callbacks: Lifecycle.Callbacks) = registry.subscribe(callbacks)
 *     override fun unsubscribe(callbacks: Lifecycle.Callbacks) = registry.unsubscribe(callbacks)
 *     // ... implement other LifecycleRegistry methods
 * }
 * ```
 *
 * ## Current Status
 *
 * This placeholder ensures that:
 * - The lifecycle module compiles successfully for ohosArm64 target
 * - Common code that depends on lifecycle can be shared across platforms
 * - You can implement platform-specific lifecycle handling in your app module
 *
 * For a complete implementation example, refer to the Android implementation in
 * `androidMain/AndroidExt.kt` as a reference.
 */

/**
 * Placeholder function for HarmonyOS lifecycle conversion.
 *
 * Currently this throws an exception to indicate that the actual implementation
 * is needed. You can override this in your application module or implement
 * the bridge as described in the module documentation above.
 *
 * @throws NotImplementedError Always thrown, indicating implementation is required
 */
fun asEssentyLifecycleForHarmonyos(): EssentyLifecycle {
    throw NotImplementedError(
        """
        HarmonyOS lifecycle integration is not implemented yet. To enable it:

        1. Add HarmonyOS SDK dependency to your project
        2. Implement the lifecycle bridge (see documentation in HarmonyosExt.kt)
        3. Use LifecycleRegistry directly in your HarmonyOS Ability code

        Example:
        class MyAbility : Ability() {
            val lifecycle = LifecycleRegistry()

            override fun onStart(intent: Intent) {
                super.onStart(intent)
                lifecycle.onCreate()
                lifecycle.onStart()
            }
            // ... handle other lifecycle events
        }
        """.trimIndent()
    )
}

/**
 * Creates a LifecycleRegistry instance for manual control on HarmonyOS.
 *
 * This is the recommended approach for HarmonyOS apps - create a LifecycleRegistry
 * in your Ability and manually control its state based on HarmonyOS lifecycle callbacks.
 *
 * Example usage:
 * ```kotlin
 * class MyAbility : Ability() {
 *     val lifecycle = LifecycleRegistry()
 *
 *     override fun onStart(intent: Intent) {
 *         super.onStart(intent)
 *         lifecycle.onCreate()
 *     }
 *
 *     override fun onActive() {
 *         super.onActive()
 *         lifecycle.onStart()
 *         lifecycle.onResume()
 *     }
 *
 *     override fun onInactive() {
 *         super.onInactive()
 *         lifecycle.onPause()
 *     }
 *
 *     override fun onStop() {
 *         super.onStop()
 *         lifecycle.onStop()
 *         lifecycle.onDestroy()
 *     }
 * }
 * ```
 */
fun createHarmonyosLifecycleRegistry(): LifecycleRegistry =
    LifecycleRegistry()
