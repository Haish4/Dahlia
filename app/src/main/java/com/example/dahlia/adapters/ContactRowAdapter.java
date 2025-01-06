package com.example.dahlia.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.databinding.ItemContactRowBinding;
import com.example.dahlia.models.EmergencyContact;
import com.example.dahlia.utilities.SimpleTextWatcher;

import java.util.ArrayList;
import java.util.List;

public class ContactRowAdapter extends RecyclerView.Adapter<ContactRowAdapter.ViewHolder> {

    private final List<EmergencyContact> contactList;

    public ContactRowAdapter() {
        this.contactList = new ArrayList<>();
    }

    public List<EmergencyContact> getContactList() {
        return contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactRowBinding binding = ItemContactRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyContact contact = contactList.get(position);
        holder.binding.editName.setText(contact.getName());
        holder.binding.editPhone.setText(contact.getPhoneNumber());

        holder.binding.editName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contact.setName(s.toString());
            }
        });

        holder.binding.editPhone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contact.setPhoneNumber(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void addContactRow() {
        contactList.add(new EmergencyContact("", ""));
        notifyItemInserted(contactList.size() - 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemContactRowBinding binding;

        ViewHolder(ItemContactRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
