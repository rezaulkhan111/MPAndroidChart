package com.github.mikephil.charting.renderer

import com.github.mikephil.charting.utils.ViewPortHandler

/**
 * Abstract baseclass of all Renderers.
 *
 * @author Philipp Jahoda
 */
abstract class Renderer {
    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected var mViewPortHandler: ViewPortHandler? = null

    constructor(viewPortHandler: ViewPortHandler?) {
        mViewPortHandler = viewPortHandler
    }
}