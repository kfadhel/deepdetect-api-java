# deepdetect API Java Client
deepdetect API Java Client is a java client for DeepDetect (http://www.deepdetect.com/): a machine learning API and server. It is a client implementation for all deepdetect server calls.

For the convenience of non-maven developers the following links are provided:

[deepdetect-api-0.0.1-SNAPSHOT bundle](https://sourceforge.net/projects/deepdetect-api-java/files/) bundle contains deepdetect-api jar with all the required 3rd-party dependencies.
[deepdetect-api-0.0.1-SNAPSHOT jar](https://sourceforge.net/projects/deepdetect-api-java/files/) a single jar with all dependencies included.

## Usage
Please look at [the example package] (https://github.com/kfadhel/deepdetect-api-java/tree/master/samples/com/deepdetect/api/samples) for examples of use case.

## Run examples
The source code contains a list of examples colling the api. To run a test you need first to download, build and start the DeepDetect server, prepare the needed dataset or trained model.

Let's take the example of CharacterBasedDeepConvolutionalNeuralNetworks class example, it permits the sentiment analysis of text passed as parameters.
* you need first to prepare environment by [following these steps](http://www.deepdetect.com/applications/text_model), make sure to download the english model from 'List of Character-Based Text Classification Models' section and unzip it to /path/to/model
* start DeepDetect server on default port (8080)
* open console and run 
<pre>
java -cp deepdetect-api.jar com.deepdetect.api.samples.CharacterBasedDeepConvolutionalNeuralNetworks /path/to/model "text1" "text 2" "text number n"
</pre>

## Feedback
All bugs, feature requests, pull requests, feedback, etc., are welcome, [create an issue](https://github.com/kfadhel/deepdetect-api-java/issues).
