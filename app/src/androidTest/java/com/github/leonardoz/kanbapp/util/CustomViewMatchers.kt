package com.github.leonardoz.kanbapp.util

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.EditorAction
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import com.google.android.material.textfield.TextInputLayout


class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (this.resources != null) {
                    try {
                        idDescription = this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        idDescription = String.format(
                            "%s (resource updateBoardName not found)",
                            *arrayOf<Any>(Integer.valueOf(recyclerViewId))
                        )
                    }

                }
                description.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View): Boolean {
                this.resources = view.resources
                if (childView == null) {
                    val recyclerView = view.rootView.findViewById(recyclerViewId) as RecyclerView
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView?.findViewById<View>(targetViewId)
                    view === targetView
                }
            }
        }
    }
}

fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        public override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout) {
                return false
            }

            val error = view.error ?: return false

            val hint = error.toString()

            return expectedErrorText == hint
        }

        override fun describeTo(description: Description) {}
    }
}


