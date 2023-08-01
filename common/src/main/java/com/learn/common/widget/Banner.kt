package com.learn.common.widget

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.learn.common.R
import com.learn.common.log.LogUtils
import com.learn.common.utils.dp2px

class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RoundFrameLayout(context, attrs, defStyleAttr, defStyleRes),
    Handler.Callback,
    ViewPager.OnPageChangeListener {

    companion object {
        private const val MSG_FLIP = 110
        private const val BANNER_FLIP_INTERVAL = 3000
        private const val BANNER_START_DELAY = 1500
        private const val INDEX_PAGER = 0
        private const val INDEX_INDICATOR = 1
    }

    private val handler: Handler

    private var innerPadding: Int

    private var aspectRadioWidth = 32

    private var aspectRadioHeight = 10

    private var heightRatio: Float = 0.75f // 宽高比
        set(value) {
            if (value != heightRatio) {
                field = value
                requestLayout()
            }
        }

    private var flippingInterval = BANNER_FLIP_INTERVAL
        set(value) {
            field = if (value <= 0) {
                BANNER_FLIP_INTERVAL
            } else {
                value
            }
        }

    private var viewPager: ViewPager? = null

    private var adapter: BannerPagerAdapter? = null

    private var bannerChildren: MutableList<IBannerItem> = ArrayList()

    private var flipping = false

    private var bannerClickListener: OnBannerClickListener? = null
        set(value) {
            field = value
            adapter?.bannerClickListener = value
        }

    private var bannerSlideListener: OnBannerSlideListener? = null
        set(value) {
            field = value
        }

    init {
        handler = Handler(this)
        innerPadding = 8f.dp2px(context).toInt()
        applyAttribute(attrs)
        initViewPager()
        initAdapter()
    }

    private fun initAdapter() {
        if (adapter == null) {
            adapter = BannerPagerAdapter(bannerChildren)
        }
    }

    private fun initViewPager() {
        viewPager = ViewPager(context).apply {
            id = ViewCompat.generateViewId()
            pageMargin = innerPadding
            offscreenPageLimit = 1
        }
        addViewInLayout(
            viewPager,
            INDEX_PAGER,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun applyAttribute(attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.Banner)
        aspectRadioWidth = typeArray.getInt(R.styleable.Banner_aspectRadioWidth, 36)
        aspectRadioHeight = typeArray.getInt(R.styleable.Banner_aspectRadioHeight, 10)
        flippingInterval = typeArray.getInt(R.styleable.Banner_flipInterval, BANNER_FLIP_INTERVAL)
        if (flippingInterval < 0) {
            flippingInterval = 0
        }
        heightRatio = aspectRadioHeight / aspectRadioWidth.toFloat()
        innerPadding = typeArray.getDimensionPixelSize(0, innerPadding)
        typeArray.recycle()
    }

    fun setBannerItems(banners: MutableList<BannerItem>?) {
        val newSize = banners?.size ?: 0
        if (newSize == 0) {
            return
        }

        val oldSize: Int = bannerChildren.size
        bannerChildren.clear()
        bannerChildren.addAll(banners!!)
//        mIndicator.setRealSize(bannerChildren.size)
        if (adapter != null && bannerChildren.size > 0) {
            adapter?.addAll(bannerChildren)
            adapter?.notifyDataSetChanged()
        }
        if (oldSize == 0) {
            requestLayout()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // set the image views size
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height: Int = (width * heightRatio).toInt()

        val viewpager = getChildAt(INDEX_PAGER)
        if (bannerChildren.size > 0) {
            measureChild(
                viewpager,
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
        }

        val indicator = getChildAt(INDEX_INDICATOR)
        measureChild(
            indicator,
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(width, height)
    }

    override fun handleMessage(msg: Message): Boolean {
        TODO("Not yet implemented")
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageSelected(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onPageScrollStateChanged(state: Int) {
        TODO("Not yet implemented")
    }

    class BannerPagerAdapter(list: List<IBannerItem>) : PagerAdapter(), OnClickListener {
        companion object {
            const val TAG = "BannerPagerAdapter"
        }

        private val bannerItem: ArrayList<IBannerItem> = ArrayList(6)

        var bannerClickListener: OnBannerClickListener? = null

        init {
            addAll(list)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val bannerItem = getItem(position)
            val view = bannerItem.getView(container)
            view.tag = bannerItem
            view.setOnClickListener(this)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }

        override fun getCount(): Int = Int.MAX_VALUE

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return `object` == view
        }

        fun addAll(list: List<IBannerItem>?) {
            if (list != null) {
                bannerItem.clear()
                bannerItem.addAll(list)
            } else {
                LogUtils.e(TAG, "addAll: list is null")
            }
        }

        private fun getItem(position: Int): IBannerItem {
            return bannerItem[getBannerPosition(position)]
        }

        private fun getBannerPosition(position: Int): Int {
            return position % bannerItem.size
        }

        override fun onClick(view: View) {
            bannerClickListener?.onClick(view.tag as IBannerItem)
        }
    }

    interface OnBannerClickListener {
        fun onClick(item: IBannerItem)
    }

    interface OnBannerSlideListener {
        fun onSlideTo(item: IBannerItem)
    }

    interface IBannerItem {
        fun getView(viewGroup: ViewGroup): View
    }

    abstract class BannerItem : IBannerItem {
        private var itemViewCaches: SparseArray<View>? = null

        abstract fun createItemView(viewGroup: ViewGroup): View

        abstract fun reuseItemView(view: View)

        override fun getView(viewGroup: ViewGroup): View {
            if (itemViewCaches == null) {
                itemViewCaches = SparseArray(4)
            }
            val cache = itemViewCaches!!
            var itemView: View? = null
            for (i in 0 until cache.size()) {
                itemView = cache.valueAt(i)
                itemView = if (itemView.parent == null) { // remove from container
                    break
                } else {
                    null // try next one
                }
            }
            if (itemView == null) { // all had been added to container
                // create a new one
                itemView = createItemView(viewGroup)
                if (itemView.id == NO_ID) {
                    itemView.id = ViewCompat.generateViewId()
                }
                cache.put(itemView.id, itemView)
            } else {
                reuseItemView(itemView)
            }
            return itemView
        }

        fun onDestroy() {
            itemViewCaches?.clear()
            itemViewCaches = null
        }
    }
}