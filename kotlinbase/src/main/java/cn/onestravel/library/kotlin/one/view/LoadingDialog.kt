package cn.onestravel.library.kotlin.one.view

import android.app.Dialog
import android.content.Context
import android.os.Handler
import androidx.annotation.StyleRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.onestravel.library.kotlin.one.R
import com.bumptech.glide.Glide

import java.lang.ref.WeakReference


/**
 * Created by Administrator on 2016/5/5.
 */
open class LoadingDialog : Dialog, View.OnClickListener {
    private var tvTitle: TextView? = null
    private var layoutContent: LinearLayout? = null
    private var isCancelable = true
    private var lp: WindowManager.LayoutParams? = null
    private var ivLoading: ImageView? = null
    public var title="正在加载..."
    set(value) { tvTitle!!.text = title}


    private constructor(context: Context, @StyleRes themeResId: Int = R.style.Dialog_View) : super(
        context,
        themeResId
    ) {
        initView(context)
    }


    private fun initView(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
        setContentView(layout)

        layoutContent = layout.findViewById(R.id.layout_content)
        ivLoading = layout.findViewById(R.id.iv_loading)
        tvTitle = layout.findViewById(R.id.tv_title)
        layoutContent!!.setOnClickListener(this)
        // 设置window属性
        lp = window!!.attributes
        lp!!.gravity = Gravity.CENTER
        lp!!.dimAmount = 0f // 去背景遮盖
        lp!!.alpha = 1.0f
        window!!.attributes = lp
        try {
            Glide.with(context)
                .load(R.drawable.loading_gif)
                .into(ivLoading!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * 设置是否可以点击取消
     *
     * @param isCancelable
     */
    override fun setCancelable(isCancelable: Boolean) {
        this.isCancelable = isCancelable
    }

    override fun dismiss() {
        Handler().postDelayed({
            try {
                super.dismiss()
            } catch (e: Exception) {

            }
        }, 200)
    }

    override fun onClick(v: View) {
        if (isCancelable) {
            dismiss()
        }
    }

    fun destroy(){
        if (sWeakReferenceInstance != null) {
            sWeakReferenceInstance!!.clear()
            sWeakReferenceInstance = null
        }
    }

    companion object {
        private var sWeakReferenceInstance: WeakReference<LoadingDialog>? = null

        fun getInstance(context: Context): LoadingDialog {
            if (sWeakReferenceInstance != null) {
                sWeakReferenceInstance!!.clear()
                sWeakReferenceInstance = null
            }
            sWeakReferenceInstance = WeakReference(LoadingDialog(context, R.style.Dialog))
            return sWeakReferenceInstance!!.get()!!
        }
    }

}
