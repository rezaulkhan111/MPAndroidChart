package com.github.mikephil.charting.utils

import com.github.mikephil.charting.utils.ObjectPool.Poolable

/**
 * Class for describing width and height dimensions in some arbitrary
 * unit. Replacement for the android.Util.SizeF which is available only on API >= 21.
 */
class FSize : Poolable {

    var width = 0f
    var height = 0f

    companion object {
        private var pool: ObjectPool<FSize>? = null

        @JvmStatic
        fun getInstance(width: Float, height: Float): FSize {
            val result = pool!!.get()!!
            result.width = width
            result.height = height
            return result
        }

        @JvmStatic
        fun recycleInstance(instance: FSize) {
            pool!!.recycle(instance)
        }

        fun recycleInstances(instances: List<FSize>?) {
            pool!!.recycle(instances!!)
        }

        init {
            pool = ObjectPool.create(256, FSize(0F, 0F)) as ObjectPool<FSize>
            pool!!.setReplenishPercentage(0.5f)
        }
    }

    override fun instantiate(): Poolable {
        return FSize(0F, 0F)
    }

    constructor() {}

    constructor(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (this === obj) {
            return true
        }
        if (obj is FSize) {
            val other = obj
            return width == other.width && height == other.height
        }
        return false
    }

    override fun toString(): String {
        return width.toString() + "x" + height
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        return java.lang.Float.floatToIntBits(width) xor java.lang.Float.floatToIntBits(height)
    }
}