package by.litvin.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import by.litvin.R;
import by.litvin.activity.CharacterDetailActivity;
import by.litvin.databinding.CharacterRecyclerItemBinding;
import by.litvin.listeners.MoveAndSwipeListener;
import by.litvin.model.Character;

//TODO try to implement using ListAdapter
public class CharactersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MoveAndSwipeListener {

    public static final int TYPE_NORMAL = 1;
    private static final int TYPE_LOADING = 2;
    public static final String CHARACTER = "character";

    private Context context;
    private List<Character> characters;

    public CharactersRecyclerViewAdapter(Context context) {
        this.context = context;
        this.characters = new ArrayList<>();
    }

    public void setCharacterItems(List<Character> characters) {
        this.characters.addAll(characters);

        //TODO not a best way, change later (noitfy insert etc)
        notifyDataSetChanged();
    }

    public void addNullDataForProgressBar() {
        characters.add(null);
        notifyItemInserted(characters.size() - 1);
    }

    public void removeNullDataForProgressBar() {
        characters.remove(characters.size() - 1);
        notifyItemInserted(characters.size());
    }

    public List<Character> getCharacters() {
        return characters;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            CharacterRecyclerItemBinding binding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.character_recycler_item, parent, false);
            return new CharacterCardView(binding);
        } else {
            View footerProgressBar = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_recycler_footer, parent, false);
            return new FooterView(footerProgressBar);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CharacterCardView) {
            Character character = characters.get(position);
            CharacterCardView characterViewHolder = (CharacterCardView) viewHolder;
            characterViewHolder.bind(character);
            CharacterRecyclerItemBinding binding = characterViewHolder.binding;

            binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(context, CharacterDetailActivity.class);
                intent.putExtra(CHARACTER, character);
                Pair imageTransition = Pair.create(binding.characterPhoto, context.getString(R.string.character_image_transition_name));
                context.startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation((Activity) context, imageTransition).toBundle());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (characters.get(position) != null) {
            return TYPE_NORMAL;
        } else {
            return TYPE_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //TODO implement item move
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        //TODO do something with swipe logic
        notifyDataSetChanged();
    }

    public class CharacterCardView extends RecyclerView.ViewHolder {
        private CharacterRecyclerItemBinding binding;

        public CharacterCardView(CharacterRecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Character character) {
            binding.setCharacter(character);
            binding.executePendingBindings();
        }
    }

    public class FooterView extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public FooterView(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }
}
