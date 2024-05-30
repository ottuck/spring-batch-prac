package com.example.SpringBatchTutorial.job.FileDataReadWrite;

import com.example.SpringBatchTutorial.job.FileDataReadWrite.dto.Player;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PlayerFieldSetMapper implements FieldSetMapper<Player> {

    @Override
    public Player mapFieldSet(FieldSet fieldSet) throws BindException {
        Player player = new Player();

        player.setID(fieldSet.readString("ID"));
        player.setLastName(fieldSet.readString("LastName"));
        player.setFirstName(fieldSet.readString("FirstName"));
        player.setPosition(fieldSet.readString("Position"));
        player.setBirthYear(fieldSet.readInt("BirthYear"));
        player.setDebutYear(fieldSet.readInt("DebutYear"));

        return player;
    }
}
