package sample;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

public class Controller  implements Initializable {
    class run extends Thread{
        @Override
        public void run() {

            super.run();
        }
    }
    @FXML
    AnchorPane pane;
    @FXML
    ToggleGroup group;
    @FXML
    RadioButton bfsRadio,dfsRadio;
    int  num=50;
    int width =270/num,height=270/num;

    Rectangle arr[][]=new Rectangle[num][num];
    int vis[][]=new int[num][num];
    ObservableList<pair> list= FXCollections.observableArrayList();
    int index=0;
    static boolean found=false,mousePressed=false,run=false;
    public void animate(ActionEvent actionEvent) throws InterruptedException {
        run=true;
        new AnimationTimer() {
            long lastTick = 0;
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    if(run)
                    update();
                    return;
                }
                if (now - lastTick > 1000000000 / 1000000) {
                    lastTick = now;
                    if(run)
                    update();
                }
            }
        }.start();
        iterativeDFS(arr,num-1,num-1);
    }

    public void reset(ActionEvent actionEvent) {
        run=false;
            draw();
        vis=new int[num][num];
        index=0;
        list= FXCollections.observableArrayList();

        found=false;
        mousePressed=false;

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
            if(arr[p.x][p.y].getFill()==Color.RED||vis[p.x][p.y]==1||found)
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
                    stack.push(new pair(p.x + 1, p.y)); //go down

                if(p.y+1<arr[0].length)
                    stack.push(new pair(p.x, p.y + 1)); //go right

                if(p.x-1>=0)
                    stack.push(new pair(p.x - 1, p.y)); //go up

                if(p.y-1>=0)
                    stack.push(new pair(p.x, p.y - 1)); //go left
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
            if(arr[p.x][p.y].getFill()==Color.RED||vis[p.x][p.y]==1||found)
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
            q.add(new pair(p.x+1,p.y));
            if(p.x-1>=0)
            q.add(new pair(p.x-1,p.y));
            if(p.y+1<arr[0].length)
            q.add(new pair(p.x,p.y+1));
            if(p.y-1>=0)
            q.add(new pair(p.x,p.y-1));

        }
    }
    void dfs(int x,int y,int ex,int ey,Rectangle [][] arr)  {
        if(arr[x][y].getFill()==Color.RED||vis[x][y]==1||found)
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
    void update(){
        if(list.get(index).x<0)
            arr[list.get(index).x*-1][list.get(index).y*-1].setFill(Color.BLUE);
        else
        arr[list.get(index).x][list.get(index).y].setFill(Color.GREEN);

        if(index+1< list.size())
        index++;
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
                r.setHeight(height*3);
                r.setWidth(width*3);
                r.setX(i*width*3.1);
                r.setY(y*width*3.1);
                r.setFill(Color.BLACK);
                r.setStroke(Color.WHITE);
                r.setStrokeWidth(0.5);
                r.setOnMousePressed(mouseEvent -> {
                    if(!mousePressed)
                    {
                        mousePressed=true;
                        r.setFill(Color.RED);
                    }
                    else
                    {
                        mousePressed=false;
                        r.setFill(Color.BLACK);
                    }


                });
                r.setOnMouseMoved(mouseEvent -> {
                    if(mousePressed)
                        r.setFill(Color.RED);
                });
            }
        for(int i=0;i<num;i++)
            for(int y=0;y<num;y++)
                pane.getChildren().add(arr[i][y]);
    }
}
