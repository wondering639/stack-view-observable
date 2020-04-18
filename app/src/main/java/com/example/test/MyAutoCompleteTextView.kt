package com.example.test

import android.content.Context
import android.os.Parcelable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.widget.Filterable
import android.widget.ListAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR


// TODO: previousText nur aktualisieren, wenn nicht custom ist!!!! IMMER!!!
// ausnahme setRealValue, da verlasse drauf, dass nicht custom ist
class MyAutoCompleteTextView(context: Context, attrs: AttributeSet) : AppCompatAutoCompleteTextView(context, attrs), Observable {
    private val TAG ="MyAutoCompleteTextView"

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    // TODO was ist mit null? wie später unterscheiden null und "" ? gar nicht? denke an viewmodel, was hat das immer als wert?
    public var previousText: String = ""


    //public var myRealValue: String
    // TODO direkt get / set mgl?

    //@InverseBindingAdapter(attribute = "myRealValue", event = "android:textAttrChanged") // TODO: attr changed gute idee?


    var realValue: String = ""
    @Bindable get(): String {
        Log.i(TAG, "real value get called")
        return field
    }
    set(value: String) {
        Log.i(TAG, "real value set called")
        field=value // this creates backing field
        setText(value)
        notifyPropertyChanged(BR.realValue)
    }


    var myValue: String = ""
        @Bindable get(): String {
            return myValue
        }
        set(value: String) {
            field=value
            setText(value)
            notifyPropertyChanged(BR.myValue) // my View implements the Observable interface
        }



    private var watcher: TextWatcher? = null

    // kann direkt von außen gesetzt werden
    public var dialogCallback: (() -> Unit)? = null

    override fun <T : ListAdapter?> setAdapter(adapter: T) where T : Filterable? {
        super.setAdapter(adapter)
    }

    override fun setText(text: CharSequence?, filter: Boolean) {
        // TODO: ab in methode? storeTextIfNotCustom ?
        if(this.text.toString() != context.getString(R.string.custom)) {
                previousText = this.text.toString() // oder alternativ TODO charsequence storen?
            }

        // unbedingt erst, nachdem alten text gesichert habe
        super.setText(text, filter)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if(this.text.toString() != context.getString(R.string.custom)) {
            previousText = this.text.toString() // oder alternativ TODO charsequence storen?
        }

        // unbedingt erst, nachdem alten text gesichert habe
        super.setText(text, type)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        Log.i(TAG, "onTextChanged to $text, savedstate = $isSaveEnabled savefromparent = $isSaveFromParentEnabled")

        // brauche toString, denn CharSequence hat kein Operator overload, selbst equals geht auf Referenz
        // https://stackoverflow.com/questions/18690835/how-to-compare-a-charsequence-to-a-string-android
        if(text.toString() == context.getString(R.string.custom)) {

            Log.i(TAG, "invoking dialogCallback")

            dialogCallback?.invoke()

            // TODO: das hier wäre evtl schneller:
            //setOnItemClickListener()

        }

        // TODO: prüfe ob onclickhandler besser/früher einspringt
        // bräuchte wahrscheinlich view.tag der so?
        // tag kann wohl selbst objekte halten bzw eigene keys definieren..
        // setTag kann key angeben..
        // https://stackoverflow.com/questions/5291726/what-is-the-main-purpose-of-settag-gettag-methods-of-view

    }




    override fun onSaveInstanceState(): Parcelable? {
        Log.i(TAG, "onsave")
        // remove listener?
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        Log.i(TAG, "onrestore")
        super.onRestoreInstanceState(state)
        //add listener?
    }

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }


}