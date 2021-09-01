package org.jetbrains.skija

open class Rect internal constructor(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    val width: Float
        get() = right - left
    val height: Float
        get() = bottom - top

    fun intersect(other: Rect): Rect? {
        return if (right <= other.left || other.right <= left || bottom <= other.top || other.bottom <= top) null else Rect(
            Math.max(
                left, other.left
            ), Math.max(top, other.top), Math.min(
                right, other.right
            ), Math.min(bottom, other.bottom)
        )
    }

    fun scale(scale: Float): Rect {
        return scale(scale, scale)
    }

    fun scale(sx: Float, sy: Float): Rect {
        return Rect(left * sx, top * sy, right * sx, bottom * sy)
    }

    fun offset(dx: Float, dy: Float): Rect {
        return Rect(left + dx, top + dy, right + dx, bottom + dy)
    }

    fun offset(vec: Point): Rect {
        return offset(vec.x, vec.y)
    }

    fun toIRect(): IRect {
        return IRect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    open fun inflate(spread: Float): Rect {
        return if (spread <= 0) makeLTRB(
            left - spread, top - spread, Math.max(
                left - spread, right + spread
            ), Math.max(top - spread, bottom + spread)
        ) else RRect.Companion.makeLTRB(
            left - spread, top - spread, Math.max(left - spread, right + spread), Math.max(
                top - spread, bottom + spread
            ), spread
        )
    }

    val isEmpty: Boolean
        get() = right == left || top == bottom

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is Rect) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        if (java.lang.Float.compare(left, other.left) != 0) return false
        if (java.lang.Float.compare(top, other.top) != 0) return false
        if (java.lang.Float.compare(right, other.right) != 0) return false
        return if (java.lang.Float.compare(bottom, other.bottom) != 0) false else true
    }

    protected open fun canEqual(other: Any?): Boolean {
        return other is Rect
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        result = result * PRIME + java.lang.Float.floatToIntBits(left)
        result = result * PRIME + java.lang.Float.floatToIntBits(top)
        result = result * PRIME + java.lang.Float.floatToIntBits(right)
        result = result * PRIME + java.lang.Float.floatToIntBits(bottom)
        return result
    }

    override fun toString(): String {
        return "Rect(_left=" + left + ", _top=" + top + ", _right=" + right + ", _bottom=" + bottom + ")"
    }

    companion object {
        @JvmStatic
        fun makeLTRB(l: Float, t: Float, r: Float, b: Float): Rect {
            require(l <= r) { "Rect::makeLTRB expected l <= r, got $l > $r" }
            require(t <= b) { "Rect::makeLTRB expected t <= b, got $t > $b" }
            return Rect(l, t, r, b)
        }

        @JvmStatic
        fun makeWH(w: Float, h: Float): Rect {
            require(w >= 0) { "Rect::makeWH expected w >= 0, got: $w" }
            require(h >= 0) { "Rect::makeWH expected h >= 0, got: $h" }
            return Rect(0f, 0f, w, h)
        }

        @JvmStatic
        fun makeWH(size: Point): Rect {
            return makeWH(size.x, size.y)
        }

        @JvmStatic
        fun makeXYWH(l: Float, t: Float, w: Float, h: Float): Rect {
            require(w >= 0) { "Rect::makeXYWH expected w >= 0, got: $w" }
            require(h >= 0) { "Rect::makeXYWH expected h >= 0, got: $h" }
            return Rect(l, t, l + w, t + h)
        }
    }
}