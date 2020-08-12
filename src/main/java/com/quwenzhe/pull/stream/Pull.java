package com.quwenzhe.pull.stream;

/**
 * @Description 负责把source、sink建立连接
 * @Author quwenzhe
 * @Date 2020/8/5 5:52 PM
 */
public class Pull {

    public static <R> Source<R> pull(Duplex<R> duplex, Stream... streams) {
        return pull(duplex.source(), streams);
    }

    public static <R> Source<R> pull(Source<R> source, Stream... streams) {
        for (int i = 0; i < streams.length - 1; i++) {
            Through<R> through = (Through) streams[i];
            source = through.transform(source);
        }

        Stream stream = streams[streams.length - 1];
        if (stream instanceof Sink) {
            Sink<R> sink = (Sink) stream;
            sink.read(source);
            return null;
        } else if (stream instanceof Duplex) {
            Duplex<R> duplex = (Duplex) stream;
            duplex.sink().read(source);
            return null;
        } else {
            Through<R> through = (Through) stream;
            return through.transform(source);
        }
    }
}
