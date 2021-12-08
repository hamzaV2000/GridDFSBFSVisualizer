package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class Controller  implements Initializable {
    @FXML
    AnchorPane pane;
    @FXML
    ToggleGroup group;
    @FXML
    RadioButton bfsRadio,dfsRadio;
    int  num=50;
    double width =270/num*3.3,height=270/num*3.3;
    Rectangle arr[][]=new Rectangle[num][num];
    int vis[][]=new int[num][num];
    ObservableList<pair> list= FXCollections.observableArrayList();
    int index=0;
    Map<pair,pair>map=new HashMap<>();
    static boolean found=false,mousePressed=false,run=false;
    Color walls=Color.BLACK,maze=Color.WHITE;
    
    public void animate(ActionEvent actionEvent) throws InterruptedException {



        run=true;
        new AnimationTimer() {
            long lastTick = 0;
            boolean b;
            public void handle(long now) {
                if ((now - lastTick) > 100000000) {
                    lastTick = now;
                    if(run)
                    {
                        b =update();
                    }
                    if(!b||!run) {
                        stop();
                    }
                }
            }
        }.start();
    }

    public void reset(ActionEvent actionEvent) {

        run=false;

        vis=new int[num][num];
        index=0;
        list= FXCollections.observableArrayList();
        found=false;
        mousePressed=false;
        pane.getChildren().removeAll();
        pane.getChildren().clear();
        draw();
    }

    class pair{
        int x;
        int y;

        public pair(int x,int y) {
            this.x = x;
            this.y=y;
        }
    }
    void iterativeDFS(Rectangle [][]arr,int ey,int ex) throws InterruptedException {
        int h = arr.length;
        if (h == 0)
            return;
        int l = arr[0].length;
        Stack<pair> stack = new Stack<>();
        stack.push(new pair(0,0));
        while(!stack.isEmpty()&&!found)
        {
            pair p=stack.pop();
            if(arr[p.x][p.y].getFill()==walls||vis[p.x][p.y]==1||found)
                continue;
            vis[p.x][p.y]=1;


            list.add(p);
            if(p.x==ex&&ey==p.y)
            {
                found=true;
                p.x*=-1;
                p.y*=-1;

            }
            if(p.x>=0&&p.y>=0) {

                if(p.x+1<arr.length)
                {
                    pair child=new pair(p.x + 1, p.y);
                    stack.push(child); //go down
                    map.put(child,p);
                }
                if(p.y-1>=0)
                {
                    pair child=new pair(p.x, p.y - 1);
                    stack.push(child); //go left
                    map.put(child,p);
                }

                if(p.y+1<arr[0].length)
                {
                    pair child=new pair(p.x, p.y + 1);
                    stack.push(child); //go down
                    map.put(child,p);
                }

                if(p.x-1>=0)
                {
                    pair child=new pair(p.x - 1, p.y);
                    stack.push(child); //go down
                    map.put(child,p);
                }


            }
        }
    }
    void BFS(Rectangle [][]arr,int ey,int ex) throws InterruptedException {
        int h = arr.length;
        if (h == 0)
            return;
        int l = arr[0].length;
        Queue<pair> q=new LinkedList<>();
        q.add(new pair(0,0));
        while(!q.isEmpty()&&!found)
        {
            pair p=q.poll();
            if(arr[p.x][p.y].getFill()==walls||vis[p.x][p.y]==1||found)
                continue;
            vis[p.x][p.y]=1;

            list.add(p);
            if(p.x==ex&&ey==p.y)
            {
                found=true;
                p.x*=-1;
                p.y*=-1;
            }
            if(p.x+1<arr.length)
            {
                pair child=new pair(p.x+1,p.y);
                map.put(child,p);
                q.add(child);
            }
            if(p.x-1>=0)
            {
                pair child=new pair(p.x-1,p.y);
                map.put(child,p);
                q.add(child);
            }
            if(p.y+1<arr[0].length)
            {
                pair child=new pair(p.x,p.y+1);
                map.put(child,p);
                q.add(child);
            }
            if(p.y-1>=0)
            {
                pair child=new pair(p.x,p.y-1);
                map.put(child,p);
                q.add(child);
            }

        }
    }
    void dfs(int x,int y,int ex,int ey,Rectangle [][] arr)  {
        if(arr[x][y].getFill()==walls||vis[x][y]==1||found)
            return;

        vis[x][y]=1;
        if(x==ex&&ey==y)
        {
            found=true;
            x*=-1;
            y*=-1;
        }
        list.add(new pair(x,y));
if(x>=0&&y>=0){
    if(y+1<arr[0].length)
        dfs(x,y+1, ex, ey,arr);

    if(x+1<arr.length)
        dfs(x+1,y, ex, ey,arr);

    if(x-1>=0)
        dfs(x-1,y, ex, ey,arr);

    if(y-1>=0)
        dfs(x,y-1, ex, ey,arr);
}

    }
    boolean update(){
        pair p=list.get(index);
        if(p.x<0)
        {
            int distance=0;
            arr[p.x*-1][p.y*-1].setFill(Color.BLUE);
            for(int i=0;i<map.size();i++)
            {
                pair x=map.get(p);
                if(x==null)
                    continue;
                arr[x.x][x.y].setFill(Color.PURPLE);
                distance++;
                p=x;
            }
            System.out.println("distance : "+distance);
            return false;
        }

            ScaleTransition st=new ScaleTransition(Duration.millis(400),arr[p.x][p.y]);
            st.setFromX(1);
            st.setFromY(1);
            st.setToX(0.33);
            st.setToY(0.33);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.play();
            arr[p.x][p.y].setFill(Color.GREEN);



        if(index+1< list.size())
        index++;
        return true;
    }
    public void apply(ActionEvent actionEvent) throws InterruptedException {
        if(bfsRadio.isSelected())
            BFS(arr,num-1,num/2);
        else
            iterativeDFS(arr,num-1,num/2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        draw();
    }

    void draw(){
        for(int i=0;i<num;i++)
            for(int y=0;y<num;y++)
            {
                arr[i][y]=new Rectangle();
                Rectangle r=arr[i][y];
                r.setHeight(height);
                r.setWidth(width);
                r.setX(i*width);
                r.setY(y*height);
                r.setFill(maze);
                r.setStroke(Color.GRAY);
                r.setStrokeWidth(0.5);

                r.setOnMousePressed(mouseEvent -> {
                    if(!mousePressed)
                    {
                        mousePressed=true;
                        r.setFill(walls);
                    }
                    else
                    {
                        mousePressed=false;
                        r.setFill(maze);
                    }


                });
                r.setOnMouseMoved(mouseEvent -> {
                    if(mousePressed)
                        r.setFill(walls);
                });
            }
        for(int i=0;i<num;i++)
            for(int y=0;y<num;y++)
                pane.getChildren().add(arr[i][y]);


            Random rand=new Random();
        for(int y=0;y<num*20;y++)
            arr[rand.nextInt(num)][rand.nextInt(num)].setFill(walls);

    }
}
