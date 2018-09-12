#!/usr/bin/perl

use warnings;
use strict;
use Data::Dumper;
use JSON::PP;
#use File::Slurp;
#use JSON::XS;
#use Text::CSV;
#use JSON::MaybeXS;


 # read accounts
 my @accounts = readAccounts();
# print Dumper(\@accounts);
 
 # write account and transactions
 for my $account (@accounts) {
 	print "-" x 80 ."\n";
 	my $folderName = createDirectory($account->{'accountNumber'} ."-". $account->{'name'});
	my $transFilename = 'transactions-'. $account->{'accountNumber'} .'.json';
	my $transactions = readTransactions($transFilename); 	
	dumpTransactions($folderName, $transactions);
 }
 exit;

sub dumpTransactions {
 my ($folderName, $jsonTransactions) = @_;
 foreach my $trans (@$jsonTransactions) {
# 	warn("---- one trans: ". Dumper($trans) ."\n");
 	dumpTransaction($folderName, $trans);
 }
}

sub saveFile {
 my ($folderName, $filename, $buffer) = @_;
 $filename = $folderName .'/'. escapeFilename($filename);
 warn("** Writing file: ". $filename ."\n");
 open my $fh, '>', $filename or die;
 print $fh $buffer;
 close $fh;
}

sub escapeFilename {
 my ($filename) = @_;
 $filename =~ s/ /_/g;
 return $filename;
}

sub dumpTransaction {
 my ($folderName, $transaction) = @_;
 my $filename = $transaction->{'archiveReference'} ."-l ". $transaction->{'description'} .'.sdv';

 # warn("** ". Dumper($transaction) ."\n");
 my $buffer;
 $buffer .= $transaction->{'accountingDate'};
 $buffer .= ",". $transaction->{'description'};
 $buffer .= ",". $transaction->{'amount'}->{'amount'};
 $buffer .= ",". $transaction->{'archiveReference'} ."";
 $buffer .= "\n";
 saveFile($folderName, $filename, $buffer);
}

sub readTransactions {
 my ($filename) = @_;
 my $jsonTransactions = read_file( $filename );
 my $transactions  = JSON::PP::decode_json($jsonTransactions);
 return $transactions->{'transactions'};
}

sub readAccounts {
 my $jsonAccounts = read_file( 'accounts.json' );
 my $accounts  = JSON::PP::decode_json($jsonAccounts);
 my @accountList;
 foreach my $account (@{$accounts->{'accounts'}}) {
 	push(@accountList, {
 		'id' => $account->{'id'},
 		'name' => $account->{'name'},
 		'accountNumber' => $account->{'accountNumber'}->{'value'},
 	});
 }
 return @accountList;

}

sub createDirectory {
 my ($name) = @_;
 my $nameEscaped = $name;
 $nameEscaped =~ s/ /_/g;
 warn("** Creating $nameEscaped ($name)\n");
 mkdir($nameEscaped) || warn("Cant create folder '$nameEscaped': $!\n");
 return $nameEscaped;
}

sub read_file {
 my ($filename) = @_;
 open my $fh, '<', $filename or die;
 $/ = undef;
 my $data = <$fh>;
 close $fh;
 return $data;
}
