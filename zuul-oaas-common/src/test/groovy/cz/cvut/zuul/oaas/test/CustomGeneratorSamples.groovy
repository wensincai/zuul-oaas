package cz.cvut.zuul.oaas.test

class CustomGeneratorSamples {

    static String anyEmail() { CustomGenerators.emails().next() }

    static Date anyFutureDate() { CustomGenerators.futureDates().next() }

    static Date anyPastDate() { CustomGenerators.pastDates().next() }
}
