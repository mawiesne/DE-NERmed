# DE-NERmed

[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/mawiesne/DE-NERmed/main/LICENSE)
[![Build Status](https://github.com/mawiesne/DE-NERmed/actions/workflows/maven.yml/badge.svg)](https://github.com/mawiesne/DE-NERmed/actions)
[![Contributors](https://img.shields.io/github/contributors/mawiesne/DE-NERmed)](https://github.com/mawiesne/DE-NERmed/graphs/contributors)
[![GitHub pull requests](https://img.shields.io/github/issues-pr-raw/mawiesne/DE-NERmed.svg)](https://github.com/mawiesne/DE-NERmed/pulls)

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
For a first impression, just execute `DENerDemo.java` which will, by default, load the [DE-NERmed-Wiki_2023_medium-maxent.bin](https://download.it.hs-heilbronn.de/de-nermed/DE-NERmed-Wiki_2023_medium-maxent.bin) 
model resource. The `NamedEntityRecognizer` instance will then find the NEs for German nouns from the (bio)medical domain.

> [!IMPORTANT]  
> For reasons of limited LFS storage, no model file will be included in the `models` directory of this Git repository, if you clone this repository.
> You will have to download all other [model files](https://download.it.hs-heilbronn.de/de-nermed/) separately.

Once retrieved, place those model files in the `models` directory to start experimenting with it.

In the demo example, the German sentence `Der Urin des Patienten ist rot verfärbt.` will be processed. 
The results are logged to STD out / console. It should be similar to:
 
```
INFO [main] OpenNLPModelServiceImpl(50) - Importing NLP model file 'DE-NERmed-Wiki_2023-maxent.bin' ...
INFO [main] DENerMedDemo(50) - Detecting NEs for: 'Der Urin des Patienten ist rot verfärbt.'
INFO [main] DENerMedDemo(80) - Found NE 'Urin' - [pos: 2, prob: 0,94]
```

Once the default examples are processed, you can enter your text fragments for testing via an interactive mode.
Hit `q` to quit the demo program and free up RAM on your local machine.

## How to obtain the German model files?
The complete set of files consists of two models:
     
| Model name                              | F1     | Acc    | Binary size | RAM required | External download required                                                                |
|-----------------------------------------|--------|--------|-------------|--------------|-------------------------------------------------------------------------------------------|
| DE-NERmed-Wiki_2023-maxent.bin          | 0.8761 | 0.8922 | 342.2MB     | ~4096MB      | [Yes](https://download.it.hs-heilbronn.de/de-nermed/DE-NERmed-Wiki_2023-maxent.bin)        |
| DE-NERmed-Wiki_2023-medium-maxent.bin   | 0.8543 | 0.8754 | 57.6MB      | ~672MB       | [Yes](https://download.it.hs-heilbronn.de/de-nermed/DE-NERmed-Wiki_2023_medium-maxent.bin) |

_Table 1: Relevant properties of DE-NERmed models: performance (F1, Accuracy), binary size and memory required._

## Training details
During the preparation phase, a synthetic text corpus was compiled, comprising of close to 88.000 health-related Wikipedia articles 
and its German full texts, dating July 2023. The corpus contained ~2.4 mio sentences.
Next, these raw texts were automatically pre-annotated based on a full [UMLS](https://www.nlm.nih.gov/research/umls/index.html) concept list; 
it included 56.600 medical NEs, translated into German.

The training of NER models was conducted based on the open-source NLP toolkit [Apache OpenNLP](https://opennlp.apache.org).
For the generation of NER models, the OpenNLP training parameters were chosen as follows:

```
training.algorithm=maxent
training.iterations=300
training.cutoff=3
training.threads=8
use.token.end=false
language=de
```

The resulting binary model files were persisted for evaluation and later re-use in NLP
applications with a NER component.


## Evaluation details
For the performance evaluation of the DE-NERmed models, _n=101_ text fragments were randomly selected from discharge letters, originally created in the Chest Pain Unit at the Heidelberg University Hospital.
For inclusion, a text fragment had to consist of at least 20 tokens. For each sample, relevant, that is _true positive_ and _true negative_, medical concepts were manually annotated by the author of this work.

> [!NOTE]  
> However, for legal (data protection) reasons, the Eval corpus cannot be made public or passed on to individuals or third parties.


The (large) model (`DE-NERmed-Wiki_2023-maxent.bin`) achieved an F1 score of _0.8761_ and an Accuracy of _0.8922_ (TP=905; TN=1214; FP=65; FN=191), see Table 1 above. 
It detected most of the relevant medical NEs, associated with the cardiology and the general medical domain.
Misclassifications occurred primarily for NEs which were representative for both, the general and the medical language.


## How to cite?
If you use one of the **DE-NERmed** models of this project, or the code of this repository in your scientific work, please cite the
[GMDS 2024](https://gesundheit-gemeinsam.de) paper as follows:

> :memo: <br>
Wiesner M. _DE-NERmed: A Named Entity Recognition Model for the Detection of German Medical Entities_.
Deutsche Gesellschaft für Medizinische Informatik, Biometrie und Epidemiologie. 69. Jahrestagung der Deutschen Gesellschaft für Medizinische Informatik, Biometrie und Epidemiologie e.V. (GMDS). 
>Dresden, 08.-13.09.2024. Düsseldorf: German Medical Science GMS Publishing House; 2024.
> 
> Accepted for publication, DOI: _pending_
