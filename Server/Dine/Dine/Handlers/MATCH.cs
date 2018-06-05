using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine.Handlers
{
    public class MATCH : IHandler
    {
        public void Invoke(User u, Reader r)
        {
            try
            {
                string id = r.readStr();
                string name = r.readStr();
                if (String.IsNullOrEmpty(u.getId()))
                    u.setId(id);
                u.setName(name);

                int isTwo = r.read16();
                int isThree = r.read16();
                int isFour = r.read16();
                int nine_ten = r.read16();
                int cowell_stevenson = r.read16();
                int crown_merrill = r.read16();
                int porter_kresge = r.read16();
                int rc_oakes = r.read16();

                MatchQuery matchQuery = new MatchQuery(u.getId());
                if (isTwo == 1)
                    matchQuery.insertGroupSizes(2);
                if (isThree == 1)
                    matchQuery.insertGroupSizes(3);
                if (isFour == 1)
                    matchQuery.insertGroupSizes(4);
                if (nine_ten == 1)
                    matchQuery.updatePreference("nine/ten");
                if (cowell_stevenson == 1)
                    matchQuery.updatePreference("cowell/stevenson");
                if (crown_merrill == 1)
                    matchQuery.updatePreference("crown/merill");
                if (porter_kresge == 1)
                    matchQuery.updatePreference("porter/kresge");
                if (rc_oakes == 1)
                    matchQuery.updatePreference("rc/oakes");


                List<string> partners = Matching.tryMatch(matchQuery);

                if (partners.Count == 0) // No Match Found
                {
                    Writer w = new Writer(0x02);
                    w.write((byte)0);
                    u.send(w);
                }
                else
                {


                    string chatId = "";
                    do
                    {
                        chatId = Util.generateId();

                    } while (Messenger.getChat(chatId) != null);

                    Chat c = new Chat(chatId);
                    c.add(u.getId(), u);

                    u.send(getMatch(chatId));
                    foreach (string p in partners)
                    {
                        User x = Server.getUser(p);
                        if (x == null)
                            continue;
                        c.add(p, x);
                        x.send(getMatch(chatId));
                    }
                    Messenger.add(chatId, c);
                }
            }
            catch { }
        }

        private Writer getMatch(string chatId)
        {
            Writer w = new Writer(0x02);
            w.write((byte)1);
            w.writeStr(chatId);
            return w;
        }
    }
}
