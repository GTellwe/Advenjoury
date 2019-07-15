package com.silvergruppen.photoblog.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.layouts.LineView;
import com.silvergruppen.photoblog.viewmodels.HowToViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class HowtoFragment extends Fragment {

    // views
    private LinearLayout taskTreeContainer;
    private LinearLayout adjustableTaskTreeContainer;
    private ImageButton addTaskButton;
    private RelativeLayout mainContainer;
    private ConstraintLayout expandedImage;
    private Button addAchievementButton;
    private EditText achievementNameEditTextView;
    private Button subtaskHeadlineButton;

    // lists
    ArrayList<ArrayList<Achievement>> taskTree;
    HashMap<String, ArrayList<View>> achievementNameToGetViewlist;
    HashMap<View,String> viewToGetAchiementName;

    // strings
    private String userId;

    // fields for the dragging and panning
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    float scalediff;

    // other
    private HowToViewModel viewModel;
    private Achievement achievement;
    private Animator currentAnimator;
    private int shortAnimationDuration = 200;
    private Achievement currentActiveSubTask;



    public HowtoFragment() {
        // Required empty public constructor
        achievementNameToGetViewlist = new HashMap<>();
        viewToGetAchiementName = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_howto, container, false);
        taskTreeContainer = view.findViewById(R.id.task_tree_container);
        adjustableTaskTreeContainer = view.findViewById(R.id.adjustable_layout);

        // initialize animations views
        expandedImage = view.findViewById(R.id.add_achievement_expanded_view);
        mainContainer = view.findViewById(R.id.container);
        achievementNameEditTextView = view.findViewById(R.id.new_achievement_text);

        // get the add task button and set up the on click listener
       /* subtaskHeadlineButton = view.findViewById(R.id.subtask_headline_button);
        addTaskButton = view.findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentActiveSubTask = new Achievement(subtaskHeadlineButton.getText().toString());
                showHideTaskList(view);

            }
        });
*/


        // initialize view holder and load data into the tree arrays
        viewModel = ViewModelProviders.of(this).get(HowToViewModel.class);
        viewModel.init(userId, achievement);
        final Observer<HashMap<String, ArrayList<Achievement>>> taskTreeObserver = new Observer<HashMap<String, ArrayList<Achievement>>>() {
            @Override
            public void onChanged(@Nullable final HashMap<String, ArrayList<Achievement>> newTaskTree) {
                uppdateTaskTreeView2(newTaskTree,adjustableTaskTreeContainer);
            }
        };


        // when the add achievement button is pressed, add the achievement to firebase and reload the fragment
        addAchievementButton = view.findViewById(R.id.add_new_achievement_button);
        addAchievementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the name of the achievement
                if(achievementNameEditTextView.getText() == null) {
                    Toast.makeText(getContext(), "Plaese name the achievement", Toast.LENGTH_LONG).show();
                    return;
                }

                // tell the view model to update task tree and odd a task tree observer to the reesulting live data woch updates the view
                Toast.makeText(getContext(), "adding the task", Toast.LENGTH_LONG).show();
                viewModel.addAchievementToTaskTree(userId, achievement, currentActiveSubTask, new Achievement(achievementNameEditTextView.getText().toString())).observe(getActivity(), taskTreeObserver);
            }
        });
        viewModel.getTaskTree().observe(this,taskTreeObserver);


        // handle the drag and zoom effects of the task tree
        taskTreeContainer.setOnTouchListener(new View.OnTouchListener() {

            RelativeLayout.LayoutParams parms;
            int startwidth;
            int startheight;
            float dx = 0, dy = 0, x = 0, y = 0;
            float angle = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {


            final LinearLayout view = (LinearLayout) v;

                //((BitmapDrawable) view.getDrawable()).setAntiAlias(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:

                        parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        startwidth = parms.width;
                        startheight = parms.height;
                        dx = event.getRawX() - parms.leftMargin;
                        dy = event.getRawY() - parms.topMargin;
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            mode = ZOOM;
                        }

                        //d = rotation(event);

                        break;
                    case MotionEvent.ACTION_UP:

                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {

                            x = event.getRawX();
                            y = event.getRawY();

                            parms.leftMargin = (int) (x - dx);
                            parms.topMargin = (int) (y - dy);

                            parms.rightMargin = 0;
                            parms.bottomMargin = 0;
                            parms.rightMargin = parms.leftMargin + (5 * parms.width);
                            parms.bottomMargin = parms.topMargin + (10 * parms.height);

                            view.setLayoutParams(parms);

                        } else if (mode == ZOOM) {

                            if (event.getPointerCount() == 2) {

                                //newRot = rotation(event);
                                float r = newRot - d;
                                angle = r;

                                x = event.getRawX();
                                y = event.getRawY();

                                float newDist = spacing(event);
                                if (newDist > 10f) {
                                    float scale = newDist / oldDist * view.getScaleX();
                                    if (scale > 0.6) {
                                        scalediff = scale;
                                        view.setScaleX(scale);
                                        view.setScaleY(scale);

                                    }
                                }

                                view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();

                                x = event.getRawX();
                                y = event.getRawY();

                                parms.leftMargin = (int) ((x - dx) + scalediff);
                                parms.topMargin = (int) ((y - dy) + scalediff);

                                parms.rightMargin = 0;
                                parms.bottomMargin = 0;
                                parms.rightMargin = parms.leftMargin + (5 * parms.width);
                                parms.bottomMargin = parms.topMargin + (10 * parms.height);

                                view.setLayoutParams(parms);


                            }
                        }
                        break;
                }

                return true;

            }
        });

        return view;
    }

    private void uppdateTaskTreeView2(HashMap<String, ArrayList<Achievement>> newTaskTree, LinearLayout adjustableTaskTreeContainer){

        ArrayList<Achievement> children = newTaskTree.get("goal");

        if(children == null)
            return;

        // add the root achievement goal to the hashmap
        Achievement parentAchievement = new Achievement("goal",children,addAchievementButton);

        // If a tree is already showing, remove it
        adjustableTaskTreeContainer.removeAllViews();

        // clear the task tre buttons hashmap
        achievementNameToGetViewlist.clear();

        // create a linked list with all the achievements and fill in each achievements parent and children and level
        LinkedList<Achievement> linkedList = new LinkedList<>();
        addParentsAndChildrenRecursion(newTaskTree, parentAchievement,0);
        ArrayList<Achievement> tmp = new ArrayList<>();
        tmp.add(parentAchievement);
        linkedListRecursionFunction(newTaskTree, tmp, linkedList);


        // initialize all the branch views
        initializeBranchViews(linkedList);

        // crete the view tree
        recursivelyCreateAllViews(linkedList);

        // add tree view to the layout file
        adjustableTaskTreeContainer.addView(parentAchievement.getBranchView());

        // test code
        //createBranch(parentAchievement);
        //adjustableTaskTreeContainer.addView(parentAchievement.getBranchView());


    }
    private void initializeBranchViews(LinkedList<Achievement> achievements){


        for (int i =0; i< achievements.size(); i++) {
           achievements.get(i).setBranchView(createLeafView(achievements.get(i)));

        }

    }

    private void recursivelyCreateAllViews(LinkedList<Achievement> linkedList){

        Achievement achievement = linkedList.pollLast();

        //Log.d("size"+linkedList.size(),"\n \n \n \n saize");

        if(achievement == null)
            return;

        if(achievement.getChildAchievements() != null && achievement.getChildAchievements().size() != 0) {
            createBranch(achievement);
            Log.d(achievement.getName()+" "+achievement.getChildAchievements().size(),"\n \n \n \n saize");

        }


        recursivelyCreateAllViews(linkedList);

    }

    private void createBranch(Achievement parent){

        ArrayList<Achievement> children = parent.getChildAchievements();
        // create the outmost container
        ConstraintLayout mainContainer = new ConstraintLayout(getContext());
        ConstraintLayout.LayoutParams mainContainerParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        mainContainer.setLayoutParams(mainContainerParams);
        ConstraintSet constraintSet = new ConstraintSet();
        mainContainer.setId(View.generateViewId());

        // crete the parent achievement leaf
        View parentView = createLeafView(parent);

        // add parent view to the main container and constraint the parent view
        mainContainer.addView(parentView);
        constraintSet.clone(mainContainer);
       //constraintSet.connect(parentView.getId(),ConstraintSet.TOP, mainContainer.getId(),ConstraintSet.TOP);
        constraintSet.connect(parentView.getId(),ConstraintSet.LEFT, mainContainer.getId(),ConstraintSet.LEFT);
        constraintSet.connect(parentView.getId(),ConstraintSet.RIGHT, mainContainer.getId(),ConstraintSet.RIGHT);
        constraintSet.applyTo(mainContainer);

        // create the linear layput for the children and add the children
        LinearLayout childrenContainer = new LinearLayout(getContext());
        childrenContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineParams.gravity = Gravity.TOP;
        lineParams.setMargins(0,40,0,0);

        childrenContainer.setLayoutParams(lineParams);

       // childrenContainer.setGravity(Gravity.TOP);
        childrenContainer.setId(View.generateViewId());

        //  add children to the linear layout
        for(Achievement achievement : children){

            childrenContainer.addView(achievement.getBranchView());
        }

        // add vhildre to the main container and constrain it
        mainContainer.addView(childrenContainer);
        constraintSet.clone(mainContainer);
        constraintSet.connect(childrenContainer.getId(), ConstraintSet.TOP,parentView.getId(), ConstraintSet.BOTTOM);
        //constraintSet.connect(childrenContainer.getId(),ConstraintSet.LEFT, mainContainer.getId(),ConstraintSet.LEFT);
        //constraintSet.connect(childrenContainer.getId(),ConstraintSet.RIGHT, mainContainer.getId(),ConstraintSet.RIGHT);
        constraintSet.applyTo(mainContainer);

        // TODO: add line views
        /*
        // Create the line views for each children
        int[] parentLocation = new int[2];
        parentView.getLocationInWindow(parentLocation);
        int parentHeight =parentView.getHeight();
        int parentWidth = parentView.getWidth();

        for(Achievement achievement : children) {

            int[] childLocation = new int[2];
            achievement.getBranchView().getLocationInWindow(childLocation);
            int childHeight = achievement.getBranchView().getHeight();
            int childWidth = achievement.getBranchView().getWidth();

            LineView lineView = new LineView(getActivity(), new Point(parentLocation[0] + parentWidth / 2, parentLocation[1] + parentHeight / 2), new Point(childLocation[0] + childWidth / 2, childLocation[1] + childHeight / 2));
            lineView.setId(View.generateViewId());

            mainContainer.addView(lineView);
            constraintSet.clone(mainContainer);
            constraintSet.connect(lineView.getId(), ConstraintSet.TOP,parentView.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(lineView.getId(),ConstraintSet.LEFT, achievement.getBranchView().getId(),ConstraintSet.RIGHT);
            constraintSet.connect(lineView.getId(),ConstraintSet.RIGHT, parentView.getId(),ConstraintSet.RIGHT);
            constraintSet.applyTo(mainContainer);

        }
**/
        parent.setBranchView(mainContainer);

    }
    private View createLeafView(Achievement achievement){

        LinearLayout newTaskContainer = new LinearLayout(getContext());
        newTaskContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //lineParams.gravity = Gravity.CENTER;
        newTaskContainer.setLayoutParams(lineParams);
        newTaskContainer.setGravity(Gravity.TOP);
        newTaskContainer.setId(View.generateViewId());

        // set the layput parameters of the headline button
        Button newTaskButton = new Button(getContext());
        LinearLayout.LayoutParams butParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        butParams.setMargins(dpsToPixels(5),dpsToPixels(5),dpsToPixels(5),dpsToPixels(5));
        newTaskButton.setLayoutParams(butParams);
        newTaskButton.setBackgroundResource(R.drawable.button_background_white_round);
        newTaskButton.setElevation(50);
        newTaskButton.setText(achievement.getName());
        Typeface font =  ResourcesCompat.getFont(getContext(), R.font.montserrat);
        newTaskButton.setTypeface(font);
        newTaskButton.setId(View.generateViewId());

        // add view to achievment
        achievement.setView(newTaskButton);

        // set the layput parameters of the add button
        ImageButton newTaskAddbutton = new ImageButton(getContext());
        LinearLayout.LayoutParams imButtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imButtParams.setMargins(0,0,dpsToPixels(15),dpsToPixels(15));
        imButtParams.height = dpsToPixels(25);
        imButtParams.width = dpsToPixels(25);
        imButtParams.gravity = Gravity.RIGHT;
        newTaskAddbutton.setLayoutParams(imButtParams);
        newTaskAddbutton.setBackgroundResource(R.drawable.round_button_background);
        newTaskAddbutton.setElevation(50);
        newTaskAddbutton.setImageResource(R.drawable.baseline_add_24);
        newTaskAddbutton.setVisibility(View.INVISIBLE);
        newTaskAddbutton.setId(View.generateViewId());

        // set the layput parameters of the minus button
        ImageButton newTaskMinusButton = new ImageButton(getContext());
        LinearLayout.LayoutParams imMinusButtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imMinusButtParams.setMargins(0,0,dpsToPixels(15),0);
        imMinusButtParams.height = dpsToPixels(25);
        imMinusButtParams.width = dpsToPixels(25);
        imMinusButtParams.gravity = Gravity.RIGHT;
        newTaskMinusButton.setLayoutParams(imMinusButtParams);
        newTaskMinusButton.setBackgroundResource(R.drawable.round_button_background);
        newTaskMinusButton.setElevation(50);
        newTaskMinusButton.setImageResource(R.drawable.minus);
        newTaskMinusButton.setVisibility(View.INVISIBLE);
        newTaskMinusButton.setId(View.generateViewId());

        //TODO: Add button listeners

        // Add the views to the task tree button hashmap
        ArrayList<View> viewList = new ArrayList<>();
        viewList.add(newTaskButton);
        viewList.add(newTaskAddbutton);
        viewList.add(newTaskMinusButton);
        achievementNameToGetViewlist.put(achievement.getName(),viewList);
        viewToGetAchiementName.put(newTaskAddbutton,achievement.getName());
        viewToGetAchiementName.put(newTaskMinusButton, achievement.getName());

        newTaskAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String achievementName = ((Button)achievementNameToGetViewlist.get(viewToGetAchiementName.get(view)).get(0)).getText().toString();
                currentActiveSubTask = new Achievement(achievementName);
                showHideTaskList(view);
            }
        });

        newTaskMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Minus button pressed",Toast.LENGTH_LONG).show();
            }
        });

        newTaskButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // show/hide the plus minus buttons
                String achievementName = ((Button) view).getText().toString();
                ImageButton plusButton = (ImageButton) achievementNameToGetViewlist.get(achievementName).get(1);
                ImageButton minusButton = (ImageButton) achievementNameToGetViewlist.get(achievementName).get(2);

                toggleVisibility(plusButton);
                toggleVisibility(minusButton);

                return false;
            }
        });


        // add view to the layout
        newTaskContainer.addView(newTaskMinusButton);
        newTaskContainer.addView(newTaskButton);
        newTaskContainer.addView(newTaskAddbutton);

        return newTaskContainer;
    }


    private void linkedListRecursionFunction(HashMap<String, ArrayList<Achievement>> newTaskTree,ArrayList<Achievement> nextLevelAchievements, LinkedList<Achievement> linkedList){

        ArrayList<Achievement> newNextLevelAchievements = new ArrayList<>();

        for(Achievement achievement : nextLevelAchievements){

            linkedList.add(achievement);
            ArrayList<Achievement> children = newTaskTree.get(achievement.getName());
            if(children != null)
                newNextLevelAchievements.addAll(children);
        }
        if(nextLevelAchievements.isEmpty())
            return;

        linkedListRecursionFunction(newTaskTree, newNextLevelAchievements, linkedList);


    }
    private void addParentsAndChildrenRecursion(HashMap<String, ArrayList<Achievement>> newTaskTree,Achievement parent, int level){



        ArrayList<Achievement> children =newTaskTree.get(parent.getName());
        parent.setTreeLevel(level);

        if(children == null) {
            return;
        }

        parent.setChildAchievements(children);

        for(Achievement achievement : children){

            achievement.setParentAchievement(parent);
            addParentsAndChildrenRecursion(newTaskTree, achievement,level++);
        }
    }

    /**
     * @param newTaskTree
     * @param adjustableTaskTreeContainer
     * Updates the task tree view from the hashmap.
     */
    private void uppdateTaskTreeView(HashMap<String, ArrayList<Achievement>> newTaskTree, LinearLayout adjustableTaskTreeContainer){

        ArrayList<Achievement> children = newTaskTree.get("goal");

        if(children == null)
            return;

        // add the root achievement goal to the hashmap
        Achievement parentAchievement = new Achievement("goal",children,addAchievementButton);

        // create buffer of achievement to loop through
        LinkedList<Achievement> buffer = new LinkedList<>();
        buffer.add(parentAchievement);
        // If a tree is already showing, remove it
        adjustableTaskTreeContainer.removeAllViews();

        // clear the task tre buttons hashmap
        achievementNameToGetViewlist.clear();

        boolean existsNextLevel = true;

        while(!buffer.isEmpty()){

            children = buffer.pop().getChildAchievements();

            RelativeLayout outerContainer = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout newLevelContainer = new LinearLayout(getContext());
            newLevelContainer.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params1.gravity = Gravity.CENTER;
            newLevelContainer.setGravity(Gravity.CENTER_HORIZONTAL);

            RelativeLayout.LayoutParams relativeParams1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            relativeParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);



            newLevelContainer.setLayoutParams(params1);

            outerContainer.addView(newLevelContainer,relativeParams1);
            ArrayList<Achievement> nextLevelChildren = new ArrayList<>();

            for(Achievement achievement : children) {

                // add the the parent achievement
                achievement.setParentAchievement(parentAchievement);

               // Toast.makeText(getContext(), " updating task tree"+achievement.getName(),Toast.LENGTH_LONG).show();
                // TODO: add achievement field to linear layout
                //TODO: add the layout parameters to the views
                // set the layput parameters of the container
                LinearLayout newTaskContainer = new LinearLayout(getContext());
                newTaskContainer.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lineParams.gravity = Gravity.CENTER;
                newTaskContainer.setLayoutParams(lineParams);
                newTaskContainer.setGravity(Gravity.CENTER);

                // set the layput parameters of the headline button
                Button newTaskButton = new Button(getContext());
                LinearLayout.LayoutParams butParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                butParams.setMargins(dpsToPixels(5),dpsToPixels(5),dpsToPixels(5),dpsToPixels(5));
                newTaskButton.setLayoutParams(butParams);
                newTaskButton.setBackgroundResource(R.drawable.button_background_white_round);
                newTaskButton.setElevation(50);
                newTaskButton.setText(achievement.getName());
                Typeface font =  ResourcesCompat.getFont(getContext(), R.font.montserrat);
                newTaskButton.setTypeface(font);

                // add view to achievment
                achievement.setView(newTaskButton);

                // set the layput parameters of the add button
                ImageButton newTaskAddbutton = new ImageButton(getContext());
                LinearLayout.LayoutParams imButtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                imButtParams.setMargins(0,0,dpsToPixels(15),dpsToPixels(15));
                imButtParams.height = dpsToPixels(25);
                imButtParams.width = dpsToPixels(25);
                imButtParams.gravity = Gravity.RIGHT;
                newTaskAddbutton.setLayoutParams(imButtParams);
                newTaskAddbutton.setBackgroundResource(R.drawable.round_button_background);
                newTaskAddbutton.setElevation(50);
                newTaskAddbutton.setImageResource(R.drawable.baseline_add_24);
                newTaskAddbutton.setVisibility(View.INVISIBLE);

                // set the layput parameters of the minus button
                ImageButton newTaskMinusButton = new ImageButton(getContext());
                LinearLayout.LayoutParams imMinusButtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                imMinusButtParams.setMargins(0,0,dpsToPixels(15),0);
                imMinusButtParams.height = dpsToPixels(25);
                imMinusButtParams.width = dpsToPixels(25);
                imMinusButtParams.gravity = Gravity.RIGHT;
                newTaskMinusButton.setLayoutParams(imMinusButtParams);
                newTaskMinusButton.setBackgroundResource(R.drawable.round_button_background);
                newTaskMinusButton.setElevation(50);
                newTaskMinusButton.setImageResource(R.drawable.minus);
                newTaskMinusButton.setVisibility(View.INVISIBLE);

                // Create the line views
                int[] parentLocation = new int[2];
                achievement.getParentAchievement().getView().getLocationInWindow(parentLocation);
                int parentHeight = achievement.getParentAchievement().getView().getHeight();
                int parentWidth = achievement.getParentAchievement().getView().getWidth();

                int[] childLocation = new int[2];
                achievement.getView().getLocationInWindow(childLocation);
                int childHeight = achievement.getView().getHeight();
                int childWidth = achievement.getView().getWidth();

                LineView lineView = new LineView(getActivity(),new Point(parentLocation[0]+parentWidth/2,parentLocation[1]+parentHeight/2), new Point(childLocation[0]+childWidth/2,childLocation[1]+childHeight/2));

                RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                outerContainer.addView(lineView, relativeParams);


                // add view to the layout
                newTaskContainer.addView(newTaskMinusButton);
                newTaskContainer.addView(newTaskButton);
                newTaskContainer.addView(newTaskAddbutton);
                newLevelContainer.addView(newTaskContainer);

                // Add the views to the task tree button hashmap
                ArrayList<View> viewList = new ArrayList<>();
                viewList.add(newTaskButton);
                viewList.add(newTaskAddbutton);
                viewList.add(newTaskMinusButton);
                achievementNameToGetViewlist.put(achievement.getName(),viewList);
                viewToGetAchiementName.put(newTaskAddbutton,achievement.getName());
                viewToGetAchiementName.put(newTaskMinusButton, achievement.getName());

                //TODO: Add button listeners
                newTaskAddbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String achievementName = ((Button)achievementNameToGetViewlist.get(viewToGetAchiementName.get(view)).get(0)).getText().toString();
                        currentActiveSubTask = new Achievement(achievementName);
                       showHideTaskList(view);
                    }
                });

                newTaskMinusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"Minus button pressed",Toast.LENGTH_LONG).show();
                    }
                });

                newTaskButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        // show/hide the plus minus buttons
                        String achievementName = ((Button) view).getText().toString();
                        ImageButton plusButton = (ImageButton) achievementNameToGetViewlist.get(achievementName).get(1);
                        ImageButton minusButton = (ImageButton) achievementNameToGetViewlist.get(achievementName).get(2);

                        toggleVisibility(plusButton);
                        toggleVisibility(minusButton);

                        return false;
                    }
                });

                // add children to the next level
                ArrayList<Achievement> chilsChildren = newTaskTree.get(achievement.getName());
                if(chilsChildren != null) {
                    achievement.setChildAchievements(chilsChildren);
                    buffer.add(achievement);
                }
            }
            //children = nextLevelChildren;
            adjustableTaskTreeContainer.addView(outerContainer);
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }
    private void showHideTaskList(final View thumbView){

        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ConstraintLayout expandedImageView = expandedImage;
        //expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        mainContainer
                .getGlobalVisibleRect(finalBounds, globalOffset);
        //expandedImageView.getGlobalVisibleRect(finalBounds);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        int offsetX = (mainContainer.getWidth()-expandedImageView.getWidth())/2;
        int offsetY = (mainContainer.getHeight()-expandedImageView.getHeight())-60;

        finalBounds.offset(-globalOffset.x+offsetX, -globalOffset.y+offsetY);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    private int dpsToPixels(int dp){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void toggleVisibility(View view){

        if(view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.INVISIBLE);
        else
            view.setVisibility(View.VISIBLE);

    }
}
