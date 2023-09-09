package com.tomst.lolly.ui.options;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.tomst.lolly.R;
import com.tomst.lolly.databinding.FragmentOptionsBinding;

import com.tomst.lolly.utils.Tools;
import com.tomst.lolly.utils.ViewAnimation;

public class OptionsFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id)
    {
        switch (parent.getId()) {

        }

        String line = String.format("%s time: %s",modes_desc[pos],parent.getItemAtPosition(pos).toString());
        Toast.makeText(getActivity(), line , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //
    }

    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_info = null;  //  (ImageButton) findViewById(R.id.bt_toggle_info);
    private Button bt_hide_info = null;         //(Button) findViewById(R.id.bt_hide_info);
    private String[] modes_desc,down_desc ;

    private View root = null;
    private FragmentOptionsBinding binding;

    private OptionsViewModel mViewModel;


    private MaterialRadioButton[] additional;


    private View lyt_expand_info;


    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }


    /*
    private CheckBoxState checkedStatus() {
        int count = 0;
        for (MaterialRadioButton cb : additional) {
            if (cb.isChecked()) count++;
        }
        if (count == 0) {
            return CheckBoxState.UNCHECKED;
        } else if (count == additional.length) {
            return CheckBoxState.CHECKED;
        } else {
            return CheckBoxState.INDETERMINATE;
        }
    }


    private void updateParentCheckbox() {
        CheckBoxState stat = checkedStatus();
        if (stat.equals(CheckBoxState.CHECKED)) {
            checkbox_add.setButtonDrawable(R.drawable.ic_check_box);
            checkbox_add.setTag(CheckBoxState.CHECKED);
        } else if (stat.equals(CheckBoxState.UNCHECKED)) {
            checkbox_add.setButtonDrawable(R.drawable.ic_check_box_outline);
            checkbox_add.setTag(CheckBoxState.UNCHECKED);
        } else {
            checkbox_add.setButtonDrawable(R.drawable.ic_indeterminate_check_box);
            checkbox_add.setTag(CheckBoxState.INDETERMINATE);
        }
    }
     */


    /*
    private void toggleSectionText(View view) {
        boolean show = Tools.toggleArrow(view);
        if (!show) {
            ViewAnimation.expand(lyt_sub, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                }
            });
        } else {
            ViewAnimation.collapse(lyt_sub);
        }
    }

     */

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    private void toggleSectionInfo(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info);
        }
    }


    private void SaveForm(){
        Context context = getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.save_options), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // jaky je stav radiobuttonu ?
//        boolean b = binding.readAll.isChecked();
        //editor.putBoolean(getString(R.string.ReadAll),b);
        //editor.putBoolean(getString(R.string.ReadFromBookmark),b);
        //editor.putBoolean(getString(R.string.ReadFromDate),b);
//        editor.putBoolean("read_all",binding.readAll.isChecked());
//        editor.putBoolean("read_bookmark",binding.readBookmark.isChecked());
//        editor.putBoolean("read_date",binding.readDate.isChecked());



        // odkud vycitam
        int spiDownload = (int) binding.spiDownload.getSelectedItemId();
        editor.putInt("readFrom",spiDownload);


        // interval mezi merenima
        int spiInterval = (int) binding.spiInterval.getSelectedItemId();
        editor.putInt("mode",spiInterval);

        // nastav zatrzitka
//        b = binding.bookmark.isChecked();
//        editor.putBoolean("bookmark",b);

        editor.putBoolean("bookmark",binding.bookmark.isChecked());
        editor.putBoolean("showgraph",binding.showgraph.isChecked());
        editor.putBoolean("noledlight",binding.noledlight.isChecked());
        editor.putBoolean("showmicro",binding.showmicro.isChecked());
        editor.putBoolean("settime",binding.settime.isChecked());

        // nastav carku-tecku
        editor.putString("decimalseparator",binding.decimalseparator.toString());
        editor.apply();
    }

    private void ReadForm(){
        Context context = getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.save_options), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // vytahnu si cislo resource ze strings.xml
        boolean r1 = sharedPref.getBoolean("read_all",false);  // false je default, kdyz neexistuje
        boolean r2 = sharedPref.getBoolean("read_bookmark",false);  // false je default, kdyz neexistuje
        boolean r3 = sharedPref.getBoolean("read_date",false);  // false je default, kdyz neexistuje
        //if (r1) binding.readAll.setChecked(true);
        //if (r2) binding.readBookmark.setChecked(true);
        //if (r3) binding.readDate.setChecked(true);

        // jak budu vycitat
        int rx = sharedPref.getInt("readFrom",-1);
        binding.spiDownload.setSelection(rx);

        // nastaveni modu
        int i = sharedPref.getInt("mode",-1);
        binding.spiInterval.setSelection(i);

        // nastav checkboxy
        boolean b = sharedPref.getBoolean("bookmark",false);
        binding.bookmark.setChecked(b);
        binding.showgraph.setChecked(sharedPref.getBoolean("showgraph",false));
        binding.noledlight.setChecked(sharedPref.getBoolean("noledlight",false));
        binding.showmicro.setChecked(sharedPref.getBoolean("showmicro",false));
        binding.settime.setChecked(sharedPref.getBoolean("settime",false));

        String s = sharedPref.getString("decimalseparator",",");  // desetinny oddelovac
        binding.decimalseparator.setText(s);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentOptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Resources res = getResources();

        // odkud vycitam
        Spinner spiDownload = (Spinner) root.findViewById(R.id.spiDownload);
        ArrayAdapter<CharSequence> adaDownload = ArrayAdapter.createFromResource(
                this.getContext(), R.array.download_array, android.R.layout.simple_spinner_item);
        adaDownload.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adaDownload.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiDownload.setAdapter(adaDownload); // Apply the adapter to the spinner
        spiDownload.setOnItemSelectedListener(this);
        down_desc = res.getStringArray(R.array.download_array);

        // vzdalenost mezi merenimi
        Spinner spiInterval = (Spinner) root.findViewById(R.id.spiInterval);
        ArrayAdapter<CharSequence> adaInterval = ArrayAdapter.createFromResource(
                this.getContext(), R.array.modes_array, android.R.layout.simple_spinner_item);
        adaInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiInterval.setAdapter(adaInterval); // Apply the adapter to the spinner
        spiInterval.setOnItemSelectedListener(this);
        //Resources res = getResources();
        modes_desc = res.getStringArray(R.array.modes_desc);

        bt_toggle_info = (ImageButton) root.findViewById(R.id.bt_toggle_info);
        bt_toggle_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info);
            }
        });

        // nested scrollview
        nested_scroll_view = (NestedScrollView) root.findViewById(R.id.nested_scroll_view);
        lyt_expand_info = (View) root.findViewById(R.id.lyt_expand_info);

        ReadForm();

        ImageButton bt_save_form = root.findViewById(R.id.bt_save_form);
        bt_save_form.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View view){
                 SaveForm();
             }
        });
        /*
        lyt_sub = root.findViewById(R.id.lyt_sub);
        ViewAnimation.collapse(lyt_sub);   // zabal ramecek
        bt_toggle = root.findViewById(R.id.bt_toggle);
        bt_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle);
            }
        });

        additional = new MaterialRadioButton[additional_id.length];
        for (int i = 0; i < additional_id.length; i++) {
            //Log.d("com.tomst.com.ListFragment", String.valueOf(i));
            additional[i] = root.findViewById(additional_id[i]);

            additional[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateParentCheckbox();
                }
            });

        }
        */

        /*
        checkbox_add = root.findViewById(R.id.checkbox_add);
        checkbox_add.setTag(CheckBoxState.CHECKED);
        checkbox_add.setText("Read all data");
        checkbox_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_add.getTag().equals(CheckBoxState.CHECKED) || checkbox_add.getTag().equals(CheckBoxState.INDETERMINATE)) {
                    checkbox_add.setButtonDrawable(R.drawable.ic_check_box_outline);
                    checkbox_add.setTag(CheckBoxState.UNCHECKED);
                    //checkUncheckCheckBox(false);
                } else if (checkbox_add.getTag().equals(CheckBoxState.UNCHECKED)) {
                    checkbox_add.setButtonDrawable(R.drawable.ic_check_box);
                    checkbox_add.setTag(CheckBoxState.CHECKED);
                    //checkUncheckCheckBox(true);
                }
            }
        });
         */

        return root;
        //return inflater.inflate(R.layout.fragment_options, container, false);

    }

    /*
    private void checkUncheckCheckBox(boolean isChecked) {
        for (MaterialCheckBox cb : additional) {
            cb.setChecked(isChecked);
        }
    }
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OptionsViewModel.class);
        // TODO: Use the ViewModel
    }


}