import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import org.apache.hadoop.io.Writable;

public class hw5_8 {

  public static class Map1
    extends Mapper<Object, Text, IntWritable, Node>{
    private IntWritable word = new IntWritable();
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
            String[] temp  = value.toString().split("\\s+");
            Node node =  new Node(Integer.parseInt(temp[0]),true);
            Node node2 = new Node(Integer.parseInt(temp[1]),false);
            word.set(Integer.parseInt(temp[1]));
            context.write(word, node);
            word.set(Integer.parseInt(temp[0]));
            context.write(word, node2);
    }
  }

  public static class Red1
    extends Reducer<IntWritable,Node,IntWritable,Node> {
    private IntWritable word = new IntWritable();
    public void reduce(IntWritable key, Iterable<Node> values,
                                              Context context
                       ) throws IOException, InterruptedException {
      List<Integer> inputNodes = new ArrayList<>();
      List<Integer> outputNodes = new ArrayList<>();
      inputNodes.add(key.get());
    	for(Node value:values){
    		if(value.isSender){
            inputNodes.add(value.value);
      	}
      	else{
          outputNodes.add(value.value);
      	}
    	}

      for(Integer in:inputNodes){
        word.set(in);
        for(Integer out:outputNodes){
          if(!in.equals(out))
            context.write(word,new Node(out,true));
        }
      }
    }
  }

  public static class Map2 extends Mapper<Object,Text,Text,Text>{
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      String[] values = value.toString().split("\\s+");
      if(values.length >= 2)
      context.write(new Text(values[0]),new Text(values[1]));
    }
  }

  public static class Red2 extends Reducer<Text,Text,Text,Text> {
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      Set<String> output = new HashSet<String>();
      for (Text value: values) {
        output.add(value.toString());
      }
      context.write(key, new Text(output.toString()));
    }
  }


    public static void main(String[] args) throws Exception {
   
      Configuration conf1 = new Configuration();
      Job job1 = Job.getInstance(conf1);
      job1.setJobName("Job 1");
      job1.setJarByClass(hw5_8.class);
      job1.setMapperClass(Map1.class);
      job1.setReducerClass(Red1.class);
      job1.setOutputKeyClass(IntWritable.class);
      job1.setOutputValueClass(Node.class);

      Path tempOutput = new Path("temp");
      FileInputFormat.addInputPath(job1, new Path(args[0]));
      FileOutputFormat.setOutputPath(job1, tempOutput);
      tempOutput.getFileSystem(conf1).delete(tempOutput, true);
      job1.waitForCompletion(true);

      Configuration conf2 = new Configuration();
      Job job2 = Job.getInstance(conf2);
      job2.setJobName("Job 2");
      job2.setJarByClass(hw5_8.class);
      job2.setMapperClass(Map2.class);
      job2.setReducerClass(Red2.class);
      job2.setOutputKeyClass(Text.class);
      job2.setOutputValueClass(Text.class);

      Path outputPath = new Path("out");
      FileInputFormat.addInputPath(job2, tempOutput);
      FileOutputFormat.setOutputPath(job2, outputPath);
      outputPath.getFileSystem(conf2).delete(outputPath, true);
      System.exit(job2.waitForCompletion(true) ? 0 : 1);

    }

  static class Node implements Writable{
    public boolean isSender;
    public int value;

    public Node(){

    }

    public Node(int value,boolean isSender){
    	this.value = value;
    	this.isSender = isSender;
    }

    @Override
  	public void readFields(DataInput in) throws IOException {
  	    value = in.readInt();
  	    isSender = in.readBoolean();
  	}

  	@Override
  	public void write(DataOutput out) throws IOException {
  		out.writeInt(value);
  		out.writeBoolean(isSender);
  	}

  	@Override
  	public String toString() {
  		return value+"";
	  }
  }
}
                  
