package com.github.mikephil.charting.utils

import com.github.mikephil.charting.utils.ObjectPool.Poolable
import com.github.mikephil.charting.utils.MPPointD

/**
 * Point encapsulating two double values.
 *
 * @author Philipp Jahoda
 */
class MPPointD private constructor(var x: Double, var y: Double) : Poolable() {
    companion object {
        private var pool: ObjectPool<MPPointD>? = null

        @JvmStatic
        fun getInstance(x: Double, y: Double): MPPointD {
            val result = pool!!.get()!!
            result.x = x
            result.y = y
            return result
        }

        @JvmStatic
        fun recycleInstance(instance: MPPointD) {
            pool!!.recycle(instance)
        }

        fun recycleInstances(instances: List<MPPointD>?) {
            pool!!.recycle(instances!!)
        }

        init {
            pool = ObjectPool.create(64, MPPointD(0.0, 0.0)) as ObjectPool<MPPointD>
            pool!!.setReplenishPercentage(0.5f)
        }
    }

    override fun instantiate(): Poolable {
        return MPPointD(0.0, 0.0)
    }

    /**
     * returns a string representation of the object
     */
    override fun toString(): String {
        return "MPPointD, x: $x, y: $y"
    }
}