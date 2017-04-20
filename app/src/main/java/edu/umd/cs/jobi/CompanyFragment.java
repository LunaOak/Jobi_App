package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umd.cs.jobi.model.Company;

public class CompanyFragment extends Fragment{

    public static CompanyFragment newInstance() {
        CompanyFragment fragment = new CompanyFragment();
        return fragment;
    }

}
