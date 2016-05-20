import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.List;
import java.util.ArrayList;

public class hw5_6 {

  public static class TokenizerMapper
      extends Mapper<Object, Text, Text, Text>{
      public void map(Object key, Text value, Context context
                      ) throws IOException, InterruptedException {
         String[] temp  = value.toString().split("\\s+");
         context.write(new Text(temp[1]),new Text(temp[0]));
     }
  }

  public static class IntSumReducer
      extends Reducer<Text,Text,Text,Text> {
      private IntWritable word = new IntWritable();
      public void reduce(Text key, Iterable<Text> values,
                                                Context context
                         ) throws IOException, InterruptedException {
        List<String> nodes = new ArrayList<>();
        // String output = "";
      	for(Text value:values){
          if(!nodes.contains(value.toString()))
            nodes.add(value.toString());
      	}
        // if(output != "")
          // context.write(key,new Text(output));
        context.write(key,new Text(nodes.size()+""));
      }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(hw5_6.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
                  
