module kafka.lab.adapter {
    exports br.com.saggioni.lab.adapter.controller;
    exports br.com.saggioni.lab.adapter.idgenerator;
    exports br.com.saggioni.lab.adapter.repository;

    requires kafka.lab.usecase;
    requires kafka.lab.domain;
}