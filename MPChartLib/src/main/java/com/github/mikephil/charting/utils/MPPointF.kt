package com.github.mikephil.charting.utils

import android.os.Parcel
import android.os.Parcelable.Creator
import com.github.mikephil.charting.utils.ObjectPool.Poolable

/**
 * Created by Tony Patino on 6/24/16.
 */
class MPPointF : Poolable {

    @JvmField
    var x = 0f

    @JvmField
    var y = 0f

    companion object {
        private var pool: ObjectPool<MPPointF>? = null

        fun getInstance(x: Float, y: Float): MPPointF {
            val result = pool!!.get()!!
            result.x = x
            result.y = y
            return result
        }

        @JvmStatic
        fun getInstance(): MPPointF = pool!!.get()!!

        fun getInstance(copy: MPPointF): MPPointF {
            val result = pool!!.get()!!
            result.x = copy.x
            result.y = copy.y
            return result
        }

        @JvmStatic
        fun recycleInstance(instance: MPPointF) {
            pool!!.recycle(instance)
        }

        fun recycleInstances(instances: List<MPPointF>?) {
            pool!!.recycle(instances!!)
        }

        val CREATOR: Creator<MPPointF> = object : Creator<MPPointF> {
            /**
             * Return a new point from the data in the specified parcel.
             */
            override fun createFromParcel(`in`: Parcel): MPPointF {
                val r = MPPointF(0F, 0F)
                r.my_readFromParcel(`in`)
                return r
            }

            /**
             * Return an array of rectangles of the specified size.
             */
            override fun newArray(size: Int): Array<MPPointF?> {
                return arrayOfNulls(size)
            }
        }

        init {
            pool = ObjectPool.create(32, MPPointF(0F, 0F)) as ObjectPool<MPPointF>
            pool!!.setReplenishPercentage(0.5f)
        }
    }

    constructor() {}

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    /**
     * Set the point's coordinates from the data stored in the specified
     * parcel. To write a point to a parcel, call writeToParcel().
     * Provided to support older Android devices.
     *
     * @param `in` The parcel to read the point's coordinates from
     */
    fun my_readFromParcel(parcel: Parcel) {
        x = parcel.readFloat()
        y = parcel.readFloat()
    }

    fun getX(): Float {
        return x
    }

    fun getY(): Float {
        return y
    }

    override fun instantiate(): Poolable {
        return MPPointF(0F, 0F)
    }
}