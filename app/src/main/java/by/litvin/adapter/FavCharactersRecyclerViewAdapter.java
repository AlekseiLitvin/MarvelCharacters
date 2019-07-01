package by.litvin.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import by.litvin.R;
import by.litvin.databinding.CharacterRecyclerItemBinding;
import by.litvin.model.Character;

public class FavCharactersRecyclerViewAdapter extends ListAdapter<Character, FavCharactersRecyclerViewAdapter.CharacterHolder> {


    public FavCharactersRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Character> DIFF_CALLBACK = new DiffUtil.ItemCallback<Character>() {
        @Override
        public boolean areItemsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
            return oldItem.equals(newItem);
        }
    };

    public Character getCharacterAt(int postition) {
        return getItem(postition);
    }

    @NonNull
    @Override
    public CharacterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CharacterRecyclerItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.character_recycler_item, parent, false);
        return new CharacterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterHolder holder, int position) {
        Character character = getItem(position);
        holder.bind(character);

        //TODO intent to CharacterDetailActivity
        holder.binding.getRoot().setOnClickListener(view -> {

        });
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        private CharacterRecyclerItemBinding binding;

        public CharacterHolder(CharacterRecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Character character) {
            binding.setCharacter(character);
            binding.executePendingBindings();
        }
    }
}
