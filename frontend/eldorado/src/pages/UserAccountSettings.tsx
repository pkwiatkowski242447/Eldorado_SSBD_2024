import {SetStateAction, useEffect, useState} from 'react';
import SiteHeader from "@/components/SiteHeader.tsx";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useForm} from "react-hook-form";
import {Card, CardContent} from "@/components/ui/card.tsx";
import {Input} from "@/components/ui/input.tsx";
import {isValidPhoneNumber} from "react-phone-number-input/min";
import {PhoneInput} from "@/components/ui/phone-input.tsx";
import {parsePhoneNumber} from "react-phone-number-input";
import {api} from "@/api/api.ts";
import {toast} from "@/components/ui/use-toast.ts";
import {useAccount} from "@/hooks/useAccount.ts";
import {useParams} from "react-router-dom";
import {UserType} from "@/types/Users.ts";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {RolesEnum} from "@/types/TokenPayload.ts";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";

const allUserLevels: RolesEnum[] = [RolesEnum.ADMIN, RolesEnum.STAFF, RolesEnum.CLIENT];

function UserAccountSettings() {
    const [activeForm, setActiveForm] = useState(localStorage.getItem('activeForm') || 'Authentication');
    const [managedUser, setManagedUser] = useState<UserType | null>(null);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [levelToChange, setLevelToChange] = useState<RolesEnum | null>(null);
    const {id} = useParams<{ id: string }>();
    const phoneNumber = parsePhoneNumber(managedUser?.phone || '')
    const e164Number = phoneNumber?.format('E.164')
    const {t} = useTranslation();
    const [formValues, setFormValues] = useState(null);
    const [formType, setFormType] = useState(null);

    const emailSchema = z.object({
        email: z.string().email({message: t("accountSettings.wrongEmail")}),
    });

    const userDataSchema = z.object({
        name: z.string().min(2, {message: t("accountSettings.firstNameTooShort")})
            .max(50, {message: t("accountSettings.firstNameTooLong")}),
        lastName: z.string().min(2, {message: t("accountSettings.lastNameTooShort")})
            .max(50, {message: t("accountSettings.lastNameTooLong")}),
        phoneNumber: z.string().refine(isValidPhoneNumber, {message: t("accountSettings.phoneNumberInvalid")}),
    });

    useEffect(() => {
        if (id) {
            localStorage.setItem('activeForm', activeForm);
            api.getAccountById(id).then(response => {
                setManagedUser(response.data);
                // console.log(response.data)
                // console.log(response.headers['etag'])
                window.localStorage.setItem('etag', response.headers['etag']);
            });
        }
    }, [activeForm, id]);

    const {getCurrentAccount} = useAccount();

    const handleDialogAction = () => {
        if (formType === 'email') {
            // @ts-expect-error - fix this
            SubmitEmail(formValues);
        } else if (formType === 'userData') {
            // @ts-expect-error - fix this
            SubmitUserData(formValues);
        }
        setAlertDialogOpen(false);
    };

    const handleRemoveClick = (level: RolesEnum) => {
        setLevelToChange(level);
        setAlertDialogOpen(true);
    };

    const handleAddClick = (level: RolesEnum) => {
        setLevelToChange(level);
        setAlertDialogOpen(true);
    };

    const onSubmitEmail = (values: SetStateAction<null>) => {
        setFormValues(values);
        // @ts-expect-error - fix this
        setFormType('email');
        setAlertDialogOpen(true);
    };

    const onSubmitUserData = (values: SetStateAction<null>) => {
        setFormValues(values);
        //@ts-expect-error - fix this
        setFormType('userData');
        setAlertDialogOpen(true);
    };

    //TODO you have to manually refresh the page to see the changes
    //TODO gray out the admin button for the admin that is editing his own account
    const confirmChangeUserLevel = () => {
        if (levelToChange && managedUser) {
            if (managedUser?.userLevelsDto.some(userLevel => userLevel.roleName === levelToChange)) {
                switch (levelToChange) {
                    case RolesEnum.ADMIN:
                        api.removeLevelAdmin(managedUser.id).then(() => {
                            setAlertDialogOpen(false);
                            toast({
                                title: t("accountSettings.popUp.changeUserDataOK.title"),
                                description: t("accountSettings.popUp.changeUserDataOK.userLevelRemoved")
                            })
                        }).catch((error) => {
                            handleApiError(error);
                        });
                        break;
                    case RolesEnum.STAFF:
                        api.removeLevelStaff(managedUser.id).then(() => {
                            setAlertDialogOpen(false);
                            toast({
                                title: t("accountSettings.popUp.changeUserDataOK.title"),
                                description: t("accountSettings.popUp.changeUserDataOK.userLevelRemoved")
                            })
                        }).catch((error) => {
                            handleApiError(error);
                        });
                        break;
                    case RolesEnum.CLIENT:
                        api.removeLevelClient(managedUser.id).then(() => {
                            setAlertDialogOpen(false);
                            toast({
                                title: t("accountSettings.popUp.changeUserDataOK.title"),
                                description: t("accountSettings.popUp.changeUserDataOK.userLevelRemoved")
                            })
                        }).catch((error) => {
                            handleApiError(error);
                        });
                        break;
                }
            } else {
                switch (levelToChange) {
                    case RolesEnum.ADMIN:
                        api.addLevelAdmin(managedUser.id).then(() => {
                            setAlertDialogOpen(false);
                            toast({
                                title: t("accountSettings.popUp.changeUserDataOK.title"),
                                description: t("accountSettings.popUp.changeUserDataOK.userLevelAdded")
                            })
                        }).catch((error) => {
                            handleApiError(error);
                        });
                        break;
                    case RolesEnum.STAFF:
                        api.addLevelStaff(managedUser.id).then(() => {
                            setAlertDialogOpen(false);
                            toast({
                                title: t("accountSettings.popUp.changeUserDataOK.title"),
                                description: t("accountSettings.popUp.changeUserDataOK.userLevelAdded")
                            })
                        }).catch((error) => {
                            handleApiError(error);
                        });
                        break;
                    case RolesEnum.CLIENT:
                        api.addLevelClient(managedUser.id).then(() => {
                            setAlertDialogOpen(false);
                            toast({
                                title: t("accountSettings.popUp.changeUserDataOK.title"),
                                description: t("accountSettings.popUp.changeUserDataOK.userLevelAdded")
                            })
                        }).catch((error) => {
                            handleApiError(error);
                        });
                        break;
                }
                setAlertDialogOpen(false);
                setLevelToChange(null);
            }
        }
    }

    const formEmail = useForm({
        resolver: zodResolver(emailSchema),
    });

    const formUserData = useForm({
        resolver: zodResolver(userDataSchema),
    });

    const SubmitEmail = (values: z.infer<typeof emailSchema>) => {
        if (managedUser) {
            api.changeEmailUser(managedUser.id, values.email).then(() => {
                toast({
                    title: t("accountSettings.popUp.changeUserDataOK.title"),
                    description: t("accountSettings.popUp.changeEmailOK.text"),
                });
                if (managedUser?.id) {
                    api.getAccountById(managedUser?.id).then(response => {
                        setManagedUser(response.data);
                        // console.log(response.data)
                    });
                }
                setFormType(null);
                setFormValues(null);

            }).catch((error) => {
                handleApiError(error);
            });
        }
    };

    const SubmitUserData = (values: z.infer<typeof userDataSchema>) => {
        const etag = window.localStorage.getItem('etag');
        // console.log(managedUser?.version)
        if (managedUser && managedUser.accountLanguage && etag !== null) {
            api.modifyAccountUser(managedUser.login, managedUser.version, managedUser.userLevelsDto,
                values.name, values.lastName, values.phoneNumber, false, etag)
                .then(() => {
                    getCurrentAccount();
                    // window.location.reload()
                    toast({
                        title: "Success!",
                        description: "The account info has been successfully changed.",
                    });
                    setFormType(null);
                    setFormValues(null);

                }).catch((error) => {
                handleApiError(error);
            });
        } else {
            console.log('Account or account language is not defined');
        }
    };

    return (
        <div>
            <SiteHeader/>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">{t("accountSettings.users.table.settings.account.title")}{managedUser?.login}</h1>
                </div>
                <div
                    className="mx-auto grid w-full max-w-6xl items-start gap-6 md:grid-cols-[180px_1fr] lg:grid-cols-[250px_1fr]">
                    <nav
                        className="grid gap-4 text-sm text-muted-foreground"
                    >
                        <Button variant="link" onClick={() => setActiveForm('UserLevels')} className="text-muted-foreground transition-colors hover:text-foreground">
                            {t("accountSettings.users.table.settings.account.userLevels")}
                        </Button>
                        <Button variant="link"  onClick={() => setActiveForm('Authentication')} className="text-muted-foreground transition-colors hover:text-foreground">
                            {t("accountSettings.users.table.settings.account.authentication")}
                        </Button>
                        <Button variant="link"  onClick={() => setActiveForm('Personal Info')} className="text-muted-foreground transition-colors hover:text-foreground">
                            {t("accountSettings.users.table.settings.account.personalInfo")}
                        </Button>

                    </nav>
                    <div className="grid gap-6">
                        {activeForm === 'UserLevels' && managedUser?.userLevelsDto && (
                            <div>
                                {activeForm === 'UserLevels' && (
                                    <div>
                                        {allUserLevels.map((level: RolesEnum) => (
                                            <div key={level}>
                                                <span>{level}</span>
                                                {managedUser?.userLevelsDto.some(userLevel => userLevel.roleName === level) ? (
                                                    <Button className="text-white bg-red-500 hover:bg-red-700 m-5"
                                                            onClick={() => handleRemoveClick(level)}>
                                                        {t("accountSettings.users.table.settings.account.userLevels.remove")}
                                                    </Button>
                                                ) : (
                                                    <Button
                                                        className="text-white bg-green-500 hover:bg-green-700 m-5"
                                                        onClick={() => handleAddClick(level)}>
                                                        {t("accountSettings.users.table.settings.account.userLevels.add")}
                                                    </Button>
                                                )}
                                            </div>
                                        ))}
                                        <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                            <AlertDialogContent>
                                                <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                                <AlertDialogDescription>
                                                    {t("accountSettings.users.table.settings.block.confirm1")}
                                                    {managedUser?.userLevelsDto.some(userLevel => userLevel.roleName === levelToChange)
                                                        ? t("accountSettings.users.table.settings.account.userLevels.remove2") :
                                                        t("accountSettings.users.table.settings.account.userLevels.add2")} {levelToChange}
                                                    {t("accountSettings.users.table.settings.block.level")}
                                                    {managedUser?.userLevelsDto.some(userLevel => userLevel.roleName === levelToChange)
                                                        ? t("accountSettings.users.table.settings.block.from") : t("accountSettings.users.table.settings.block.to")}
                                                    {t("accountSettings.users.table.settings.block.confirm2")}
                                                </AlertDialogDescription>
                                                <AlertDialogAction
                                                    onClick={confirmChangeUserLevel}>{t("general.ok")}</AlertDialogAction>
                                                <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                            </AlertDialogContent>
                                        </AlertDialog>
                                    </div>
                                )}
                            </div>
                        )}
                        {activeForm === 'Authentication' && (
                            <div>
                                <Card className="mx-10 w-auto">
                                    <CardContent>
                                        <Form {...formEmail}>
                                            {// @ts-expect-error - fix this maybe
                                                <form onSubmit={formEmail.handleSubmit(onSubmitEmail)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-5">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formEmail.control}
                                                                name="email"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.users.table.settings.account.authentication.email")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={managedUser?.email} {...field} />
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}/>
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2">
                                                            {t("accountSettings.users.table.settings.account.authentication.email.change")}
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("accountSettings.confirmEmailChange")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                        {activeForm === 'Personal Info' && (
                            <div>
                                <Card className="mx-auto">
                                    <CardContent>
                                        <Form {...formUserData}>
                                            {// @ts-expect-error - fix this maybe
                                                <form onSubmit={formUserData.handleSubmit(onSubmitUserData)}
                                                      className="space-y-4">
                                                    <div className="grid gap-4 p-10">
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="name"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.users.table.settings.account.personalInfo.firstName")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={managedUser?.name} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="lastName"
                                                                render={({field}) => (
                                                                    <FormItem>
                                                                        <FormLabel
                                                                            className="text-black">{t("accountSettings.users.table.settings.account.personalInfo.lastName")}</FormLabel>
                                                                        <FormControl>
                                                                            <Input
                                                                                placeholder={managedUser?.lastname} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <div className="grid gap-2">
                                                            <FormField
                                                                control={formUserData.control}
                                                                name="phoneNumber"
                                                                render={({field}) => (
                                                                    <FormItem className="items-start">
                                                                        <FormLabel
                                                                            className="text-black text-center">{t("accountSettings.users.table.settings.account.personalInfo.phoneNumber")}</FormLabel>
                                                                        <FormControl className="w-full">
                                                                            <PhoneInput //TODO fix this
                                                                                placeholder={e164Number} {...field}/>
                                                                        </FormControl>
                                                                        <FormMessage/>
                                                                    </FormItem>
                                                                )}
                                                            />
                                                        </div>
                                                        <Button type="submit" className="w-full pb-2">
                                                            {t("accountSettings.users.table.settings.account.personalInfo.saveChanges")}
                                                        </Button>
                                                    </div>
                                                </form>
                                            }
                                        </Form>
                                    </CardContent>
                                </Card>
                                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                                    <AlertDialogContent>
                                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                                        <AlertDialogDescription>
                                            {t("accountSettings.confirmUserDataChange")}
                                        </AlertDialogDescription>
                                        <AlertDialogAction onClick={handleDialogAction}>
                                            {t("general.ok")}
                                        </AlertDialogAction>
                                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
}

export default UserAccountSettings;