package pl.marchuck.cryptoapp.receive.chooseAccount

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import android.widget.ImageView
import com.marchuck.a3xisrael.MainActivity
import com.marchuck.a3xisrael.R
import com.marchuck.a3xisrael.moveNuke.BombDirection
import com.marchuck.a3xisrael.moveNuke.MoveNukeListener

class MoveNukeBottomDialog : BottomSheetDialogFragment() {

    val TAG = "MoveNukeBottomDialog"

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_move_nuke, null)
        dialog.setContentView(contentView)

        contentView.findViewById<ImageView>(R.id.move_nuke_back)
                .setOnClickListener { dismiss() }

        val left = contentView.findViewById<ImageView>(R.id.move_nuke_left)
        val top = contentView.findViewById<ImageView>(R.id.move_nuke_top)
        val bottom = contentView.findViewById<ImageView>(R.id.move_nuke_bottom)
        val right = contentView.findViewById<ImageView>(R.id.move_nuke_right)

        left.setOnClickListener { moveNuke(BombDirection.LEFT) }
        top.setOnClickListener { moveNuke(BombDirection.TOP) }
        bottom.setOnClickListener { moveNuke(BombDirection.BOTTOM) }
        right.setOnClickListener { moveNuke(BombDirection.RIGHT) }
    }

    fun moveNuke(direction: BombDirection) {
        println("move nuke: ${direction.name}")
        if (activity is MainActivity) {
            val fragments = activity!!.supportFragmentManager.fragments
            for (f in fragments) {
                if (f is MoveNukeListener) {
                    (f as MoveNukeListener).onMoveNuke(direction.asNukeDirection())
                    dismiss()
                }
            }
        }
    }

}
