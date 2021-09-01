package org.jetbrains.skija.shaper

import org.jetbrains.skija.impl.Library.Companion.staticLoad
import org.jetbrains.skija.ManagedString
import org.jetbrains.skija.impl.Managed
import org.jetbrains.skija.impl.Native
import java.lang.ref.Reference

abstract class ManagedRunIterator<T> internal constructor(
    ptr: Long,
    text: ManagedString?,
    manageText: Boolean
) : Managed(ptr, _FinalizerHolder.PTR), MutableIterator<T> {
    companion object {
        @JvmStatic external fun _nGetFinalizer(): Long
        @JvmStatic external fun _nConsume(ptr: Long)
        @JvmStatic external fun _nGetEndOfCurrentRun(ptr: Long, textPtr: Long): Int
        @JvmStatic external fun _nIsAtEnd(ptr: Long): Boolean

        init {
            staticLoad()
        }
    }

    internal val _text: ManagedString?
    override fun close() {
        super.close()
        _text?.close()
    }

    internal fun _getEndOfCurrentRun(): Int {
        return try {
            _nGetEndOfCurrentRun(_ptr, Native.Companion.getPtr(_text))
        } finally {
            Reference.reachabilityFence(this)
            Reference.reachabilityFence(_text)
        }
    }

    override fun hasNext(): Boolean {
        return try {
            !_nIsAtEnd(_ptr)
        } finally {
            Reference.reachabilityFence(this)
        }
    }

    internal object _FinalizerHolder {
        val PTR = _nGetFinalizer()
    }

    init {
        _text = if (manageText) text else null
    }
}