package god.example.god_of_teaching.view.nav.myinfo.registermodify.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
//등록하기 할 때 나오는 스피너 꾸며주는 커스텀 어뎁터
class CustomArrayAdapter(context: Context, resource: Int, items: List<String>) : ArrayAdapter<String>(context, resource, items) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView

        if (position == 0) {
            view.setTextColor(Color.parseColor("#3Faa8a"))
        } else {
            view.setTextColor(Color.BLACK)
        }

        return view
    }
}
