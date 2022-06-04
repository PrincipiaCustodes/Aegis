package com.example.egida;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder>{
    private final Context context;
    private final File[] data;
    private final String fun;
    private String type = "";

    public FileListAdapter(Context context, File[] data, String fun){
        this.context = context;
        this.data = data;
        this.fun = fun;
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

        switch (fun){
            case "launcher":
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
                            ((FragmentActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container_for_fragments, new DecodeFragment().newInstance(selectedFile.getAbsolutePath()))
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
                break;
            case "del":
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(context) {
                            @Override
                            public void positiveAction() {
                                if(SharedPrefs.getBIOMETRICS_STATUS(context).equals(context.getString(R.string.biometrics_status_use))) {
                                    Biometrics biometrics = new Biometrics() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void nextAction() {
                                            DeleteFunction.deleteFile(selectedFile.getName());
                                            ((FragmentActivity) context).getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.container_for_fragments, new DeleteFragment().newInstance("/data/data/com.example.egida/encrypted_files/"))
                                                    .addToBackStack(null)
                                                    .commit();
                                        }
                                    };
                                    biometrics.biometricsPrompt(context);
                                    getDialog().dismiss();
                                } else {
                                    new Password(context) {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void passwordCorrectAction() {
                                            DeleteFunction.deleteFile(selectedFile.getName());
                                        }
                                    };

                                    getDialog().dismiss();
                                }
                                getDialog().dismiss();
                            }

                            @Override
                            public void negativeAction() {
                                getDialog().dismiss();
                            }
                        };
                        customAlertDialog.setAlertDialogImageId(R.drawable.icon);
                        customAlertDialog.setNewAlertDialogTittle("Deleting a file");
                        customAlertDialog.setNewAlertDialogDescription("The file will be removed from the application directory and will not be encrypted");
                        customAlertDialog.setNewAlertDialogQuestion("Are you sure you want to delete the file?");
                        customAlertDialog.setNewAlertDialogOkButton("Yep");
                        customAlertDialog.setNewAlertDialogNoButton("Nope");
                        customAlertDialog.setupAlertDialogSettings();
                        customAlertDialog.getDialog().show();
                    }
                });
                break;
            case "share":
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(context) {
                            @Override
                            public void positiveAction() {
                                ((FragmentActivity)context).getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container_for_fragments, StartServerFragment.newInstance(selectedFile.toString()))
                                        .addToBackStack(null)
                                        .commit();

                                getDialog().dismiss();
                            }

                            @Override
                            public void negativeAction() {
                                getDialog().dismiss();
                            }
                        };
                        customAlertDialog.setAlertDialogImageId(R.drawable.icon);
                        customAlertDialog.setNewAlertDialogTittle("Share a file");
                        customAlertDialog.setNewAlertDialogDescription("File will be sent to the client");
                        customAlertDialog.setNewAlertDialogQuestion("Are you sure you want to share this file?");
                        customAlertDialog.setNewAlertDialogOkButton("Yep");
                        customAlertDialog.setNewAlertDialogNoButton("Nope");
                        customAlertDialog.setupAlertDialogSettings();
                        customAlertDialog.getDialog().show();
                    }
                });
                break;
            case "open":
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        File file = new File(String.valueOf(selectedFile));
//                        Uri path = Uri.fromFile(file);
//                        Intent openIntent = new Intent(Intent.ACTION_VIEW);
//                        openIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        openIntent.setDataAndType(path, "*/*");
//                        context.startActivity(openIntent);
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        String type = "image/*";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }
}
