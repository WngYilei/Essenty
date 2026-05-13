package com.arkivanov.essenty.backhandler

/**
 * Creates a new instance of [BackHandler] for HarmonyOS platform.
 *
 * Note: This is a basic implementation that provides a [BackDispatcher]
 * for manual back event handling. HarmonyOS applications should integrate
 * with the platform's back gesture system by calling [BackDispatcher.back()]
 * when a back gesture is detected.
 *
 * @return A new instance of [BackHandler] for HarmonyOS
 */
fun BackHandler(): BackHandler =
    BackDispatcher()
