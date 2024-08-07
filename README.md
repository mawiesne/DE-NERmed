# DE-NERmed

![Build Status](https://github.com/mawiesne/DE-NERmed/actions/workflows/maven.yml/badge.svg)

DE-NERmed (_pronounced_: de:e: ner:med:) is an object-oriented named entity recognizer (NER) for German texts with a focus on the (bio)medical domain.
                
It is based on [Apache OpenNLP](https://github.com/apache/opennlp) and provides pre-trained, binary Maximum-Entropy _models_ in the corresponding directory. Those have been trained during April 2024 via 88k health-related Wikipedia articles.

## Requirements

### Build
- [Apache Maven](https://maven.apache.org) in version 3.6+

### Runtime
- Java / [OpenJDK](https://adoptium.net/de/) in version 17+
- [Apache OpenNLP](https://github.com/apache/opennlp) in version 2.3.0+ 
 
#### Notes: 
- OpenNLP releases < 2.1.0 can't reliably load the NER model files of this project! 
- Check and take care of your classpath so no older OpenNLP version is around!

## Build
Build the project via Apache Maven. 
The command for the relevant parts is `mvn clean package`.   
This should download all required dependencies which are:

1. Apache OpenNLP, 
2. Apache Commons Lang3, _and_  
3. slf4j + log4j2 bindings.

If you want to re-use the current, experimental version of **DE-NERmed** in your projects, 
execute `mvn clean install` to transport the bundled _jar_ file to your local `.m2` folder.

Note: 
You have to select one or more model files and copy it over to the execution environment.
Those models must reside in the `models` directory, as the current code inspects this directory name.
     
## Usage
For a first impression, just execute `DENerDemo.java` which will, by default, load the [DE-NERmed-Wiki_2023-maxent.bin](models%2FDE-NERmed-Wiki_2023-maxent.bin) 
model resource. The `NamedEntityRecognizer` instance will then find the NEs for German nouns from the (bio)medical domain.

In the demo example, the German sentence `Der Urin ist rot verfärbt.` will be processed. 
The results are logged to STD out / console. It should be similar to:
 
```
INFO [main] OpenNLPModelServiceImpl(50) - Importing NLP model file 'DE-NERmed-Wiki_2023-maxent.bin' ...
INFO [main] DENerMedDemo(50) - Detecting NEs for: 'Der Urin ist rot verfärbt.'
INFO [main] DENerMedDemo(80) - Found NE 'Urin' - [pos: 2, prob: 0,95]
```

Once the default examples are processed, you can enter your text fragments for testing via an interactive mode.
Hit `q` to quit the demo program and free up RAM on your local machine.

## How to obtain all German model files?
The complete set of files consists of two models:

| Model name                       | Size | External download required                                                             |
|----------------------------------|------|----------------------------------------------------------------------------------------|
| DE-NERmed-Simple_2024-maxent.bin | TODO | TODO                                                                                   |
| DE-NERmed-Wiki_2023-maxent.bin   | 362M | [Yes](https://download.it.hs-heilbronn.de/de-nermed/DE-NERmed-Wiki_2023-maxent.bin) |

If you clone this repository, only the _DE-NERmed_Simple_2024-maxent.bin_ model will be included in the `models`
directory of this Git repository. For reasons of limited storage, you'll have to download all other
[model files](https://download.it.hs-heilbronn.de/de-nermed/) separately. 
Once retrieved, place those model files in the `models` directory to start experimenting with it.

## Training details
In preparation phase, a synthetic text corpus was compiled, comprising of close to 88.000 health-related Wikipedia articles 
and its German full texts, dating July 2023. The corpus contained ~2.4 mio sentences.
Next, these raw texts were automatically pre-annotated based on a full [UMLS](https://www.nlm.nih.gov/research/umls/index.html) concept list; 
it included 56.600 medical NEs, translated into German.

The training of NER models was conducted based on the open-source NLP toolkit [Apache OpenNLP](https://opennlp.apache.org).
For the generation of NER models, the OpenNLP training parameters were chosen as follows:

```
training.algorithm=maxent
training.iterations=100
training.cutoff=3
training.threads=8
language=de
use.token.end=false
sentences.per.sample=3
```

The resulting binary model files were persisted for evaluation and later re-use in NLP
applications with a NER component.
