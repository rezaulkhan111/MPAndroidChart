package com.github.mikephil.charting.jobs

import android.view.View
import com.github.mikephil.charting.utils.ObjectPool
import com.github.mikephil.charting.utils.ObjectPool.Poolable
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
class MoveViewJob : ViewPortJob {

    companion object {
        private var pool: ObjectPool<MoveViewJob>? = null

        @JvmStatic
        fun getInstance(
            viewPortHandler: ViewPortHandler?,
            xValue: Float,
            yValue: Float,
            trans: Transformer?,
            v: View?
        ): MoveViewJob {
            val result = pool!!.get()
            result!!.mViewPortHandler = viewPortHandler
            result.xValue = xValue
            result.yValue = yValue
            result.mTrans = trans
            result.view = v
            return result
        }

        fun recycleInstance(instance: MoveViewJob?) {
            pool!!.recycle(instance!!)
        }
    }

    constructor(
        viewPortHandler: ViewPortHandler?,
        xValue: Float,
        yValue: Float,
        trans: Transformer?,
        v: View?
    ) : super(viewPortHandler, xValue, yValue, trans, v) {
    }

    override fun run() {
        pts[0] = xValue
        pts[1] = yValue
        mTrans!!.pointValuesToPixel(pts)
        mViewPortHandler!!.centerViewPort(pts, view!!)
        recycleInstance(this)
    }

    override fun instantiate(): Poolable {
        return MoveViewJob(mViewPortHandler, xValue, yValue, mTrans, view)
    }
}