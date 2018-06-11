package com.example.mikarru.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MultipleOutputWordCount {
  public static final String ZERO_OUT = "zero";
  public static final String ONE_OUT = "one";

  public static class WordCountMapper extends Mapper<Object, Text, Text, LongWritable>{
    private static final LongWritable ONE = new LongWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {
      String[] strs = value.toString().split(" ");
      for (String str : strs) {
        word.set(str);
        context.write(word, ONE);
      }
    }
  }

  public static class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
    private MultipleOutputs mos;
    private LongWritable result = new LongWritable();

    public void setup(Context context) throws IOException, InterruptedException {
      mos = new MultipleOutputs(context);
    }

    public void reduce(Text key, Iterable<LongWritable> values, Context context)
        throws IOException, InterruptedException {
      long sum = 0;
      for (LongWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
      if ((key.toString().hashCode() & 0x1) == 0) {
        mos.write(ZERO_OUT, key, result);
      } else {
        mos.write(ONE_OUT, key, result);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Usage: <input-path> <output-path>");
      System.exit(1);
    }
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(MultipleOutputWordCount.class);
    job.setMapperClass(WordCountMapper.class);
    job.setReducerClass(WordCountReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);
    job.setInputFormatClass(TextInputFormat.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    job.setOutputFormatClass(TextOutputFormat.class);
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    MultipleOutputs.addNamedOutput(job, ZERO_OUT, TextOutputFormat.class,
        Text.class, LongWritable.class);
    MultipleOutputs.addNamedOutput(job, ONE_OUT, SequenceFileOutputFormat.class,
        String.class, LongWritable.class);

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
