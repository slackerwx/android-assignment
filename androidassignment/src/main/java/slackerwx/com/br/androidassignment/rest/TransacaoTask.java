package slackerwx.com.br.androidassignment.rest;

/**
 * Created by slackerwx on 06/07/15.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class TransacaoTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "HeinekenApp";
    private final Context context;
    private final Transacao transacao;
    private int aguardeMsg;
    private ProgressBar progress;
    private ProgressDialog progressDialog;
    private boolean exibirProgressDialog;

    public TransacaoTask(Context context, Transacao transacao) {
        this.context = context;
        this.transacao = transacao;
    }

    public TransacaoTask(Context context, Transacao transacao, int aguardeMsg) {
        this.context = context;
        this.transacao = transacao;
        this.aguardeMsg = aguardeMsg;
        this.exibirProgressDialog = true;
    }

    public TransacaoTask(Context context, Transacao transacao, ProgressBar progress) {
        this.context = context;
        this.transacao = transacao;
        this.progress = progress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (exibirProgressDialog) {
            abrirProgressDialog();
        } else {
            abrirProgress();
        }
    }

    private void abrirProgressDialog() {
        progressDialog = ProgressDialog.show(context, "", context.getString(aguardeMsg), true);

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                Log.d("TASK", "canceled inside listener");
                progressDialog = null;
            }
        });
    }

    private void abrirProgress() {
        if (progress != null) {
            progress.setIndeterminate(true);
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {
        try {
            transacao.executar();
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        } finally {
            try {
                if (exibirProgressDialog) {
                    fecharProgressDialog();
                } else {
                    fecharProgress();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        return true;
    }

    private void fecharProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                exibirProgressDialog = false;
            }
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void fecharProgress() {
        try {
            if (progress != null) {
                progress.setVisibility(View.INVISIBLE);
            }
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Boolean ok) {
        if (ok) {
            transacao.atualizarView();
        } else {
//			AndroidUtils.alertDialog(context, "Erro: " + exceptionErro.getMessage());
        }
    }

    protected void onCancelled(Long result) {
        Log.d("DownloadHelper", "CANCELLED result = " + result);
        fecharProgressDialog();
    }

    public void onDismiss(DialogInterface dialog) {
        Log.d("DownloadHelper", "Cancelled");
        this.cancel(true);
    }

}
