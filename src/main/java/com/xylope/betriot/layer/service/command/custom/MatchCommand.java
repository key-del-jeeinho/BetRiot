package com.xylope.betriot.layer.service.command.custom;

import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.match.ParticipantStats;
import com.merakianalytics.orianna.types.core.searchable.SearchableList;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.layer.dataaccess.apis.riot.OriannaMatchAPI;
import com.xylope.betriot.layer.dataaccess.apis.riot.SummonerAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.logic.discord.SpecialEmote;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.logic.discord.message.ChannelEmbedMessageSender;
import com.xylope.betriot.layer.logic.discord.message.ChannelMessageSenderImpl;
import com.xylope.betriot.manager.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MatchCommand extends LeafCommand {
    public MatchCommand(OriannaMatchAPI oriannaMatchAPI, SummonerAPI summonerAPI, ChannelEmbedMessageSender channelEmbedMessageSender, ChannelMessageSenderImpl channelMessageSender) {
        super(new AbstractCommand() {
                  @Override
                  public void execute(GuildChannel channel, User sender, String... args) {
                      Guild guild = channel.getGuild();
                      String channelId = channel.getId();
                      TextChannel textChannel = Objects.requireNonNull(guild.getTextChannelById(channelId));


                      int matchNumber = 1; //몇번째 전적인지

                      if(args.length == 0) {
                          channelMessageSender.sendMessage(textChannel, "전적을 보실 소환사님의 이름을 적어주세요!");
                          return;
                      }
                      if(args.length > 1) {
                          try {
                              matchNumber = Integer.parseInt(args[0]);
                              if (matchNumber == 0)
                                  throw new NumberFormatException();

                              matchNumber--;

                              args = Arrays.copyOfRange(args, 1, args.length); //args 의 0번째 인덱스(title) 를 지운다.
                          } catch (NumberFormatException e) {
                              matchNumber = 0;
                          }
                      }
                      if(matchNumber > 20) {
                          channelMessageSender.sendMessage(textChannel, "최근 20번쨰까지의 전적만 보실 수 있습니다!");
                      }
                      String summonerName = CommandManager.getRawToArgs(args);
                      SummonerDto summoner;
                      try {
                          summoner = summonerAPI.getByName(summonerName);
                      } catch (DataNotFoundException e) {
                          channelMessageSender.sendMessage(textChannel, "존재하지 않는 소환사 이름입니다.");
                          return;
                      }

                      if(summoner == null) {
                          channelMessageSender.sendMessage(textChannel, "존재하지 않는 소환사 이름입니다.");
                          return;
                      }

                      MatchHistory list = oriannaMatchAPI.getMatchListBySummoner(summoner.getId());


                      Match match = list.get(matchNumber);
                      boolean isWin;
                      AtomicReference<Participant> atomicReference = new AtomicReference<>();
                      AtomicBoolean isDataFound = new AtomicBoolean(false);
                      SearchableList<Participant> participantsRed = match.getRedTeam().getParticipants();
                      SearchableList<Participant> participantsBlue = match.getBlueTeam().getParticipants();

                      //find at red
                      for (Participant participant : participantsRed) {
                          Summoner participantSummoner = participant.getSummoner();
                          if (participantSummoner.getAccountId().equals(summoner.getAccountId())) {
                              atomicReference.set(participant);
                              isDataFound.set(true);
                          }
                      }
                      //if summoner isn't at red
                      if(!isDataFound.get()) {
                          //find at blue
                          for (Participant participant : participantsBlue) {
                              Summoner participantSummoner = participant.getSummoner();
                              if (participantSummoner.getAccountId().equals(summoner.getAccountId())) {
                                  atomicReference.set(participant);
                                  isDataFound.set(true);
                              }
                          }
                      }

                      if(!isDataFound.get()) throw new DataNotFoundException("unknown summoner account id : " + summoner.getAccountId());

                      Participant participant = atomicReference.get();

                      isWin = participant.getTeam().isWinner();
                      String winMsg = isWin ? "승리" : "패배";
                      ParticipantStats stats = participant.getStats();
                      MessageEmbed message = new EmbedBuilder()
                              .setThumbnail(participant.getChampion().getImage().getURL())
                              .setTitle(summonerName + "님의 최근" + matchNumber + "번째 전적")
                              .addField(winMsg, "게임에서 " + winMsg + "하셧습니다", false)
                              .addField("KDA",
                                      getKDADisplay(stats), false)
                              .addField("딜량",
                                      getDamageDisplay(stats), false)
                              .addField("얻은 골드 수",
                                      getGoldDisplay(stats), false)
                              .setColor(isWin ?
                                      new Color(34, 103, 222) :
                                      new Color(255, 0, 59))
                              .build();

                      channelEmbedMessageSender.sendMessage(textChannel, message);
                  }
              }
        );
    }
    private static String getKDADisplay(ParticipantStats stats) {
        return SpecialEmote.MATCH_KDA.getEmote() +
                stats.getKills() + "/" + stats.getDeaths() + "/" + stats.getAssists();
    }
    private static String getDamageDisplay(ParticipantStats stats) {
        String deal = String.format("%s %d(마법피해 : %d / 물리피해 : %d)",
                SpecialEmote.MATCH_DEAL.getEmote(),
                stats.getDamageDealtToChampions(),
                stats.getMagicDamageDealtToChampions(),
                stats.getPhysicalDamageDealtToChampions());
        if (stats.getDamageDealtToChampions() > 20000) deal = "**" + deal + "**";
        return  deal;
    }
    private static String getGoldDisplay(ParticipantStats stats) {
        return SpecialEmote.MATCH_GOLD.getEmote() + " " + stats.getGoldEarned();
    }
}

