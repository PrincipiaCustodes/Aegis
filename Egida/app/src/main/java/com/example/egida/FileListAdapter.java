package com.example.egida;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Path;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder>{
    private final Context context;
    private final File[] data;
    private String type = "";

    public FileListAdapter(Context context, File[] data){
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_file_name);
            imageView = itemView.findViewById(R.id.item_icon);
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FileListAdapter.ViewHolder holder, int position) {
        File selectedFile = data[position];
        holder.textView.setText(selectedFile.getName());

        // устанавливаем иконки
        if(selectedFile.isDirectory()){
            holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
        } else {
            if(selectedFile.getName().lastIndexOf(".") == -1){
                type = "";
            } else {
                type = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
            }

            switch (type){
                case "txt":
                case "doc":
                case "docx":
                case "pdf":
                    holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
                    break;
                case "png":
                case "jpg":
                case "jpeg":
                case "svg":
                    holder.imageView.setImageResource(R.drawable.ic_image);
                    break;
                case "mp4":
                case "gif":
                case "avi":
                    holder.imageView.setImageResource(R.drawable.ic_video_file);
                    break;
                case "mp3":
                case "wav":
                    holder.imageView.setImageResource(R.drawable.ic_audio_file);
                    break;
                case "exe":
                case "apk":
                    holder.imageView.setImageResource(R.drawable.ic_app_shortcut);
                    break;
                default:
                    holder.imageView.setImageResource(R.drawable.ic_data_object);
                    break;
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFile.isDirectory()){
                    ((FragmentActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container_for_fragments, LauncherFragment.newInstance(selectedFile.getAbsolutePath()), "LauncherFragment")
                            .addToBackStack(null)
                            .commit();
                }else{
                    try {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        String type = "image/*";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(context.getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("MOVE");
                popupMenu.getMenu().add("RENAME");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("DELETE")){
                            boolean deleted = selectedFile.delete();
                            if(deleted){
                                Toast.makeText(context.getApplicationContext(),"DELETED",Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.GONE);
                            }
                        }
                        if(item.getTitle().equals("MOVE")){
                            Toast.makeText(context.getApplicationContext(),"MOVED",Toast.LENGTH_SHORT).show();

                        }
                        if(item.getTitle().equals("RENAME")){
                            Toast.makeText(context.getApplicationContext(),"RENAME",Toast.LENGTH_SHORT).show();

                        }
                        return true;
                    }
                });

                popupMenu.show();
                return true;
            }
        });


    }
}
