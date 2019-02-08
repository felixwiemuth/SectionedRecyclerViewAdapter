package io.github.luizgrp.sectionedrecyclerviewadapter.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.CustomViewType;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import java.util.ArrayList;
import java.util.List;

public class Example9Fragment extends Fragment {

    CustomViewType custom1 = new CustomViewType(ItemViewHolder1.class, R.layout.section_ex9_item1);
    CustomViewType custom2 = new CustomViewType(ItemViewHolder2.class, R.layout.section_ex9_item2);

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex9, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter(custom1, custom2);

        for(char alphabet = 'A'; alphabet <= 'Z';alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);

            if (contacts.size() > 0) {
                sectionAdapter.addSection(new ContactsSection(String.valueOf(alphabet), contacts));
            }
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle(R.string.nav_example9);
        }
    }

    private List<String> getContactsWithLetter(char letter) {
        List<String> contacts = new ArrayList<>();

        for (String contact : getResources().getStringArray(R.array.names)) {
            if (contact.charAt(0) == letter) {
                contacts.add(contact);
            }
        }

        return contacts;
    }


    private class ContactsSection extends StatelessSection {

        String title;
        List<String> list;

        ContactsSection(String title, List<String> list) {
            super(new SectionParameters.Builder(R.layout.section_ex1_item)
                    .headerResourceId(R.layout.section_ex1_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder1(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {
                if (custom1.isInstanceOf(holder)) {
                    final ItemViewHolder1 itemHolder = (ItemViewHolder1) holder;

                    String name = list.get(position);

                    itemHolder.tvItem.setText(name);
                    itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.ic_face_black_48dp : R.drawable.ic_tag_faces_black_48dp);

                    itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s, VH1", sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (custom2.isInstanceOf(holder)) {
                    final ItemViewHolder2 itemHolder = (ItemViewHolder2) holder;

                    String name = list.get(position);

                    itemHolder.tvItem.setText(name);
                    itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.ic_face_black_48dp : R.drawable.ic_tag_faces_black_48dp);

                    itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s, VH2", sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    throw new Exception("Unrecognized ViewHolder");
                }
            } catch (Exception e) {
                Log.e(this.getClass().getSimpleName(), "onBindItemViewHolder", e);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0) {
                return sectionAdapter.getCustomViewTypeKey(custom1);
            } else {
                return sectionAdapter.getCustomViewTypeKey(custom2);
            }
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    public static class ItemViewHolder1 extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imgItem;
        private final TextView tvItem;

        ItemViewHolder1(View view) {
            super(view);

            rootView = view;
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            tvItem = (TextView) view.findViewById(R.id.tvItem);
        }
    }

    public static class ItemViewHolder2 extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imgItem;
        private final TextView tvItem;

        ItemViewHolder2(View view) {
            super(view);

            rootView = view;
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            tvItem = (TextView) view.findViewById(R.id.tvItem);
        }
    }
}
