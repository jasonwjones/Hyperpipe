# Hyperpipe

An experimental data loader for Hyperion Essbase that does not require the use of load rules.


## Usage

    java -jar hyper-pipe-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
    -sqlDriver org.h2.Driver \
    -sqlUrl jdbc:h2:tcp://localhost/~/s4me \
    -sqlUser sa \
    -sqlQuery "SELECT * FROM TESTESS" \
    -apsServer http://prdashhy03:13080/aps/JAPI \
    -olapServer prdashhy03 \
    -application Sample \
    -database Basic \
    -essUser hypuser \
    -essPass hyppass

