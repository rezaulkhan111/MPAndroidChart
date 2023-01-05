package com.github.mikephil.charting.utils

/**
 * Transformer class for the HorizontalBarChart.
 *
 * @author Philipp Jahoda
 */
class TransformerHorizontalBarChart : Transformer {

    constructor(viewPortHandler: ViewPortHandler?) : super(viewPortHandler) {
    }

    /**
     * Prepares the matrix that contains all offsets.
     *
     * @param inverted
     */
    override fun prepareMatrixOffset(inverted: Boolean) {
        mMatrixOffset.reset()
        if (!inverted) mMatrixOffset.postTranslate(
            mViewPortHandler!!.offsetLeft(),
            mViewPortHandler!!.getChartHeight() - mViewPortHandler!!.offsetBottom()
        ) else {
            mMatrixOffset
                .setTranslate(
                    -(mViewPortHandler!!.getChartWidth() - mViewPortHandler!!.offsetRight()),
                    mViewPortHandler!!.getChartHeight() - mViewPortHandler!!.offsetBottom()
                )
            mMatrixOffset.postScale(-1.0f, 1.0f)
        }
    }
}