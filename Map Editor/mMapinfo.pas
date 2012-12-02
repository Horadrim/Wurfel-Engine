unit mMapinfo;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls;

type
  TMapinfo = class(TForm)
    edMapname: TEdit;
    btSaveNew: TButton;
    laMapname: TLabel;
    edVersion: TEdit;
    laEditorversion: TLabel;
    procedure btSaveNewClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure FormCreate(Sender: TObject);
    procedure SaveInfo();
  private
    
  public
         
  end;

var Mapinfo: TMapinfo;

implementation

uses mLeveleditor, mWelcome;

{$R *.dfm}

procedure TMapinfo.btSaveNewClick(Sender: TObject);
var i:integer;
begin
    if Mapeditor.SaveDialog.Execute then begin
       Mapeditor.mapinfopath := Mapeditor.SaveDialog.FileName;
       for i:=Length(Mapeditor.mapinfopath) downto 1 do
          if Mapeditor.mapinfopath[i] = '\' then begin
             Mapeditor.mappath := Copy(Mapeditor.mapinfopath,1,i);
             break;
          end;
       SaveInfo();
    end
    else ShowMessage('Du hast abgebrochen. Das war ein Fataler Fehler. Festplatte wird formatiert!'+
    #13#10+'-Ende der schlechten Witze..');
end;

procedure TMapinfo.SaveInfo();
var Stream: TFilestream;
    Writer: TWriter;
begin
   Stream := TFilestream.Create(Mapeditor.mapinfopath,fmCreate);
   Writer := TWriter.Create(Stream,500);
   Writer.WriteString(edMapname.Text+#13#10);
   Writer.WriteString(edVersion.Text+#13#10);
   Writer.WriteString('0,0'+#13#10);
   Writer.Free;
   Stream.Free;
end;

procedure TMapinfo.FormClose(Sender: TObject; var Action: TCloseAction);
begin
   SaveInfo();
end;

procedure TMapinfo.FormCreate(Sender: TObject);
begin
   edMapname.Text := Mapeditor.mapname;
   edVersion.Text := Welcome.getVersion;
end;

end.
