
use warnings;
use strict;
use Data::Dumper;
use JSON::PP;
use File::Slurp;
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
	dumpTransactions($transactions, $folderName);
 }
 exit;

sub dumpTransactions {
 my ($jsonTransactions, $folderName) = @_;
 foreach my $trans (@$jsonTransactions) {
# 	warn("---- one trans: ". Dumper($trans) ."\n");
 	dumpTransaction($trans);
 }
}

sub dumpTransaction {
 my ($transaction) = @_;
# warn("** ". Dumper($transaction) ."\n");
 print $transaction->{'accountingDate'};
 print "\t". $transaction->{'description'};
 print "\t". $transaction->{'amount'}->{'amount'};
 print "\t(". $transaction->{'archiveReference'} .")";
 print "\n";
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