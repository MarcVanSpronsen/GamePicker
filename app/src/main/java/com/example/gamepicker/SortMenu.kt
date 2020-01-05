package com.example.gamepicker

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.sort_menu.view.*

class SortMenu(private var sortList: MutableList<Game>, private var adapter: GameAdapter) {

    var currentSortingType: SortingType = SortingType.ALPHABETIC_AZ
    private lateinit var btnSort: TextView
    private lateinit var alertDialog: AlertDialog
    private lateinit var previousSortingType: SortingType
    lateinit var view: View

    /**
     * Sets enums for all of the different sorting options
     *
     * @param textId Text id of the string.
     */
    enum class SortingType(val textId: Int) {
        /** Sort by release date ascending. */
        RELEASE_DATE_ASC(R.string.release_date_lh),
        /** Sort by release date descending. */
        RELEASE_DATE_DESC(R.string.release_date_hl),
        /** Sort alphabetically ascending */
        ALPHABETIC_AZ(R.string.alphabetic_az),
        /** Sort alphabetically descending. */
        ALPHABETIC_ZA(R.string.alphabetic_za),
    }

    /**
     * Inflates the sortingMenu.
     *
     * @param context The context in which the sortingMenu is active.
     * @param button The button which shows the current sortingType.
     * @param filteredList The list that should be sorted.
     */
    fun openMenu(context: Context, button: TextView, filteredList: MutableList<Game>) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.sort_menu, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(mDialogView)
        val  mAlertDialog = mBuilder.show()

        btnSort = button
        alertDialog = mAlertDialog
        view = mDialogView
        sortList = filteredList

        setCircleColor()
        setListeners()
    }

    /**
     * Called when text is entered in the search view.
     *
     * @param sortList The list that should be sorted.
     */
    fun sortFilteredList(sortList: MutableList<Game>) {
        onSortClick(currentSortingType, sortList)
    }

    /**
     * Sets all the onClickListeners
     *
     */
    private fun setListeners() {
        view.daysRemainingLH.setOnClickListener { onSortClick(SortingType.RELEASE_DATE_ASC, sortList)  }
        view.daysRemainingHL.setOnClickListener { onSortClick(SortingType.RELEASE_DATE_DESC, sortList) }
        view.alfabeticAZ.setOnClickListener { onSortClick(SortingType.ALPHABETIC_AZ, sortList) }
        view.alfabeticZA.setOnClickListener { onSortClick(SortingType.ALPHABETIC_ZA, sortList) }
    }

    /**
     * Handles all actions that happen when a button is clicked
     *
     * @param sortingType The sortingType which was selected by the user.
     * @param sortList The list that should be sorted.
     */
    private fun onSortClick(sortingType: SortingType, sortList: MutableList<Game>) {
        when(sortingType) {
            SortingType.RELEASE_DATE_ASC -> sortList.sortBy { it.releaseDate }
            SortingType.RELEASE_DATE_DESC -> sortList.sortByDescending { it.releaseDate }
            SortingType.ALPHABETIC_AZ -> sortList.sortBy { it.Name }
            else -> sortList.sortByDescending { it.Name }
        }
        previousSortingType = currentSortingType
        currentSortingType = sortingType
        adapter.games = sortList
        adapter.notifyDataSetChanged()

        if (currentSortingType != previousSortingType) {
            setSortBtnText(sortingType)
        }

        if (::alertDialog.isInitialized) {
            alertDialog.dismiss()
        }
    }

    /**
     * Sets the circle alpha to 1 of the selected sorting method.
     *
     */
    private fun setCircleColor() {
        when (currentSortingType) {
            SortingType.RELEASE_DATE_ASC -> view.daysRemainingAsc.alpha = 1f
            SortingType.RELEASE_DATE_DESC -> view.daysRemainingDesc.alpha = 1f
            SortingType.ALPHABETIC_AZ -> view.alfabeticAz.alpha = 1f
            else -> view.alfabeticZa.alpha = 1f
        }
    }

    /**
     * Sets the buttonText to the current sorting method.
     *
     * @param sortingType The sortingType which was selected by the user.
     */
    private fun setSortBtnText(sortingType: SortingType) {
        when(sortingType) {
            SortingType.RELEASE_DATE_ASC -> btnSort.text = view.daysRemainingLH.text
            SortingType.RELEASE_DATE_DESC -> btnSort.text = view.daysRemainingHL.text
            SortingType.ALPHABETIC_AZ -> btnSort.text = view.alfabeticAZ.text
            else -> btnSort.text = view.alfabeticZA.text
        }
    }
}